On the topic
======

[stack](http://stackoverflow.com/questions/1188587/cache-invalidation-is-there-a-general-solution)


[Martin Fowler on Aggregates](http://martinfowler.com/bliki/DDD_Aggregate.html)
>>>
Domain Driven Design aggregates are domain concepts (order, clinic visit, playlist), while collections are generic.
An aggregate will often contain mutliple collections, together with simple fields.



Objectives
====

- Simple to read the api
- easy to understand concepts
- not great performances, no cluster now with HashMaps
- other supports (infinispan, MemCache) will run faster and with cluster

I want
====

John'sProfile shows :
John, userPicture, and its tweets
John's id is : 14

key in cache is :  `profile:14`

        Profile profile = new Profile(john)
        profile.getId(); # returns john.getId()
        profile.getComments() ; # returns john's comments, including comment 28

        //Representation rep = new Gsonrepresentation(profile);
        Cache cache =  MapCache.getInstance();
        // case with Resource in the cache
        cache.add(profile, "profile:14");
        
Later :

        cache.get("profile:14"); # -> returns John's profile
        cache.invalidate("profile:14"); # -> remove from cache

        # And the problem is :
        cache.invalidate("comment:28"); # -> remove ALSO John's profile from cache


Case with representations in cache
----

A representation may be built from many Resources and make complex operations such as sorting. Thus it might be interesting to cache it.
The developper will judge if it's important or not to have stale data.
 
	Representation representation = new GsonRepresentation();
	
	for (User u : getSortedUsers()){
		representation.set(u.getId(),u);  
	}
	
	cache.put("/user/sorted", representation);
	
Once we edit a user, the representation is stale and we have NO control. 







Strategies
====

First strategy : Validation
----

- Cache has double map : cache will get bigger and slower
- Profile has an expensive and complex validation process :
    - looking through each comments Etag, and building it's own
    - or setting a date to each comment, checking the latest change

Last solution look quite easy, but Aggregate elements must all have a timer...
Setters on each classic attributes will have to change the timer. Ouch !


Second strategy : Dependency control
-----

key            | Resource
----------------------------
profile:14     | profile
user:14        | John
comment:28     | Bla bla bla on Nfl2014
comments:66543 | List<Comment> comments


Dependencies : Map of dependencies

When we caches

comment:28 | profile:14
comment:28 | topic:18  # Comment 28 is written by john and appears in topic 18
comment:27 | profile:14
comment:24 | profile:14
comment:29 | profile:14


**Rule** : Do not load directly a List from the cache, but use an aggregate with a key

        comment28.edit(....)
        cache.invalidate(comment28);
        # will removes profile:14 and topic:14;



