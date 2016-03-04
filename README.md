text-mr
=======

Various distributed text processor
# implementations
* Wikipedia page article processor

# How to build
    mvn clean package

# WikiPageProcessor
Mapping wiki page articles dump(pages-articles.xml) into TSV (id, namespace, title, comment, text) on your hadoop cluster

## Fields 
* id (page article id)
* namespace (https://en.wikipedia.org/wiki/Wikipedia:Namespace)
* title
* comment
* text (sanitized by [bliki](https://bitbucket.org/axelclk/info.bliki.wiki/wiki/Home) WikiModel)

## Usage
    # make sure that mahout-integration is in the classpath of your mapreduce cluster.
    hadoop jar path/to/target/text-mr-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.github.samuel281.text.wikipedia.WikiPageProcessor <input xml path> <output path>
    
    # or
    export LIB_JARS=/path/to/mahout-integration.jar
    hadoop jar path/to/target/text-mr-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.github.samuel281.text.wikipedia.WikiPageProcessor -libjar ${LIB_JARS} <input xml path> <output path>
    
# WikiPageParser
Parses wiki page article xml fragment and returns WikiPage object.
  
## Usage
    WikiPageParser wikiPageParser = new DefaultWikiPageParser(Locale.KOREAN, true); //to make the text single line.
    WikiPage wikiPage = wikiPageParser.parse(xml);
    System.out.println(wikiPage.getTitle());
    System.out.println(wikiPage.getRevision().getText());
   
# Add into your project as a dependecy
## Maven
    <!-- step.1 add jitpack repo -->
    <repositories>
         <repository>
             <id>jitpack.io</id>
             <url>https://jitpack.io</url>
         </repository>
    </repositories>
    
    <!-- step.2 add the dependency -->
    <dependency>
         <groupId>com.github.samuel281</groupId>
         <artifactId>text-mr</artifactId>
         <version>0.0.1-SNAPSHOT</version>
    </dependency>
    
## Gradle
    // step.1 add jitpack repo
    allprojects {
         repositories {
             ...
             maven { url "https://jitpack.io" }
         }
    }
    
    // step.2 add the dependency
    dependencies {
         compile 'com.github.samuel281:text-mr:0.0.1-SNAPSHOT'
    }