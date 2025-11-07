package Payloads;

public class Payload {

    public static String addPlace()
    {
        return ("{\n" +
                "  \"location\": {\n" +
                "    \"lat\": -38.383494,\n" +
                "    \"lng\": 33.427362\n" +
                "  },\n" +
                "  \"accuracy\": 50,\n" +
                "  \"name\": \"Anurag house\",\n" +
                "  \"phone_number\": \"(+91) 983 893 3937\",\n" +
                "  \"address\": \"29, side layout, cohen 09\",\n" +
                "  \"types\": [\n" +
                "    \"shoe park\",\n" +
                "    \"shop\"\n" +
                "  ],\n" +
                "  \"website\": \"http://rahulshettyacademy.com\",\n" +
                "  \"language\": \"French-IN\"\n" +
                "}");
    }

    public static String jsonBody()
    {
        return "{\n" +
                "\n" +
                "\"dashboard\": {\n" +
                "\n" +
                "\"purchaseAmount\": 910,\n" +
                "\n" +
                "\"website\": \"rahulshettyacademy.com\"\n" +
                "\n" +
                "},\n" +
                "\n" +
                "\"courses\": [\n" +
                "\n" +
                "{\n" +
                "\n" +
                "\"title\": \"Selenium Python\",\n" +
                "\n" +
                "\"price\": 50,\n" +
                "\n" +
                "\"copies\": 6\n" +
                "\n" +
                "},\n" +
                "\n" +
                "{\n" +
                "\n" +
                "\"title\": \"Cypress\",\n" +
                "\n" +
                "\"price\": 40,\n" +
                "\n" +
                "\"copies\": 4\n" +
                "\n" +
                "},\n" +
                "\n" +
                "{\n" +
                "\n" +
                "\"title\": \"RPA\",\n" +
                "\n" +
                "\"price\": 45,\n" +
                "\n" +
                "\"copies\": 10\n" +
                "\n" +
                "}\n" +
                "\n" +
                "]\n" +
                "\n" +
                "}";
    }

    public static String jsonBodyAddBook(String isbn, String aisle)
    {
        String body = "{\n" +
                "    \"name\": \"Learn Appium Automation with Java\",\n" +
                "    \"isbn\": \""+isbn+"\",\n" +
                "    \"aisle\": \""+aisle+"\",\n" +
                "    \"author\": \"John foe\"\n" +
                "}";
        return body;
    }
    public static String jsonBodyDeleteBook(String bookID)
    {
        String body = "{\n" +
                "    \"ID\": \""+bookID+"\"\n" +
                "}";
        return body;
    }

    public static  String createBugPayload()
    {
        String payload = "{\n" +
                "    \"fields\": {\n" +
                "       \"project\":\n" +
                "       {\n" +
                "          \"key\": \"SAMPLEKEY\"\n" +
                "       },\n" +
                "       \"summary\": \"Rest Assured Created Issue\",\n" +
                "       \"issuetype\": {\n" +
                "          \"name\": \"Bug\"\n" +
                "       }\n" +
                "   }\n" +
                "}";
        return payload;
    }

    public static String graphQLQueryPayload(int characterID, int locationID, int episodeID, String name, String epName)
    {
        String payload = "{\"query\":\"query($characterId : Int!,$locationId : Int!,$episodeId : Int!,$name : String , $epName : String)" +
                "\\n{\\n  \\n  character(characterId: $characterId){\\n    \\n    id\\n    name\\n    type\\n    status\\n    " +
                "gender\\n  }\\n  \\n  location(locationId: $locationId)\\n{\\n  name\\n  dimension\\n  id\\n}\\n  \\n  " +
                "episode(episodeId: $episodeId)\\n{\\n  \\n  name\\n  air_date\\n  episode\\n  id\\n  \\n}\\n  \\n  " +
                "characters(filters:{name: $name})\\n  {\\n    \\n    info\\n    {\\n      count\\n    }\\n    \\n    " +
                "result\\n    {\\n      name\\n      type\\n      \\n    }\\n    \\n  }\\n  episodes(filters: {name : $epName})\\n{\\n  " +
                "\\n  result\\n  {\\n\\n    id\\n    name\\n    air_date\\n    episode\\n  }\\n  \\n  \\n}\\n  \\n  \\n  \\n  \\n  \\n  " +
                "\\n  \\n  \\n  \\n}\\n\",\"variables\":{\"characterId\":"+characterID+",\"locationId\":"+locationID+",\"episodeId\":"+episodeID+"," +
                "\"name\":\""+name+"\"," +
                "\"epName\":\""+epName+"\"}}";
        return payload;
    }
    public static String graphQLMutationPayload(String locationName, String locationType,String locationDimension, String characterName,
                                                String characterType, String characterStatus, String characterSpecies, String characterGender,
                                                String characterImage, int characterOriginID, int characterLocationID,String episodeName,
                                                String episodeAirDate,String episodeCustomID, int[] locationIDForDelete)
    {
        String payload = "{\"query\":\"mutation($locationName : String!,$locationType: String!, " +
                "$locationDimension: String!,$characterName :String!,$characterType: String!, " +
                "$characterStatus : String!,$characterSpecies: String!,$characterGender: String!," +
                "$characterImage: String! , $characterOriginId : Int! , $characterlocationId: Int!,$episodeName:" +
                " String!, $episodeairDate: String!,$episodecustomID:String!,$locationIDForDelete :[Int!])" +
                "\\n{\\n  \\n  createLocation(location:{name: $locationName, type: $locationType, " +
                "dimension:$locationDimension})\\n{\\n  id\\n}\\n  \\n  createCharacter(character:" +
                "{name: $characterName,type: $characterType,status : $characterStatus, " +
                "species:$characterSpecies, gender: $characterGender,image :$characterImage," +
                "originId : $characterOriginId, locationId : $characterlocationId } )" +
                "\\n  {\\n    \\n    id  \\n  }\\n  \\n  createEpisode(episode: " +
                "{name: $episodeName,air_date:$episodeairDate, episode: $episodecustomID})\\n" +
                "{\\n  id\\n}\\n  \\n  deleteLocations(locationIds: $locationIDForDelete)\\n{\\n  " +
                "locationsDeleted\\n}\\n  \\n}\\n\\n\\n\\n\",\"variables\":{\"locationName\":\"India\",\"" +
                "locationType\":\"North\",\"locationDimension\":\"123\",\"characterName\":\"Anurag\",\"" +
                "characterType\":\"IT\",\"characterStatus\":\"Single\",\"characterSpecies\":\"human\",\"" +
                "characterGender\":\"Male\",\"characterImage\":\"png\",\"characterOriginId\":25309,\"" +
                "characterlocationId\":25309,\"episodeName\":\"TMKOC\",\"episodeairDate\":\"12-12-25\",\"" +
                "episodecustomID\":\"111\",\"locationIDForDelete\":[25313]}}";
        return payload;
    }



}
