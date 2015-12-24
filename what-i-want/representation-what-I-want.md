
Representation :
====


* Data : what we want =>  Resource, Object, InputStream, byte[]
* Document : data representation processed by the engine
* Marshaller : create a Document from the Data (kind of EntityProvider)
* Engine : transforms the document =>  stax, gson, sax, jackson : usually the Marshaller if he can
* Mapper : transforms the field names of the Document (simple transformer)
* UnMarshaller : create a Data from a Document
* Filter : apply transformations



Data => InputFilter => Marshall => PreRepresentation => Mapper => Representation => Transform => OtherRepresentation
//Note : using a Mapper allows to make everything in one stream whereas a Tranformer has not this capacity

Representation => Unmarshall => OutputFilter => Data




//Usually representation in a service will use the same


### Creating the right representation of an object

        // Or:
        RepresentationConfiguration configuration = new GsonConfiguration();


        //Then :
        Representation representation = new Representation(new GsonConfiguration(), myObject);


        //Or :
        Representation representation = new GsonRepresentation(myObject);

        // Or: global configuration of the Representation
        Representation.setConfiguration(new GsonConfiguration());
        Representation representation = new Representation(myObject);


        //Using JEE integration
        @Inject
        @GsonRepresentation
        Representation representation;

        representation = representation.createNew(myObject);



        //Mix stuff
        Representation representation = query.get();








Mapper
====



works on data to change its fielsdNames :

* dozer
* jaxb : directly on elements ; might be some xml interception available
* custom : (calls directly the engine)




