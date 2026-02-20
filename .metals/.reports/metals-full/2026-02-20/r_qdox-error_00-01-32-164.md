error id: file:///D:/ander/Documents/SEMESTRE%207/ARSW/LAB04%20-%20P1%20-%20BLUEPRINTS/Lab_P1_BluePrints_Java21_API/src/main/java/edu/eci/arsw/blueprints/dto/BlueprintDTO.java
file:///D:/ander/Documents/SEMESTRE%207/ARSW/LAB04%20-%20P1%20-%20BLUEPRINTS/Lab_P1_BluePrints_Java21_API/src/main/java/edu/eci/arsw/blueprints/dto/BlueprintDTO.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file:///D:/ander/Documents/SEMESTRE%207/ARSW/LAB04%20-%20P1%20-%20BLUEPRINTS/Lab_P1_BluePrints_Java21_API/src/main/java/edu/eci/arsw/blueprints/dto/BlueprintDTO.java
text:
```scala
m@@E PODRIASpackage edu.eci.arsw.blueprints.dto;

import java.util.List;

/**
 * Data Transfer Object for Blueprint entity.
 */
public class BlueprintDTO {
    private Long id;
    private String author;
    private String name;
    private List<PointDTO> points;

    public BlueprintDTO() {}

    public BlueprintDTO(Long id, String author, String name, List<PointDTO> points) {
        this.id = id;
        this.author = author;
        this.name = name;
        this.points = points;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<PointDTO> getPoints() { return points; }
    public void setPoints(List<PointDTO> points) { this.points = points; }
}

```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:49)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:99)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:560)
	scala.meta.internal.metals.Indexer.$anonfun$reindexWorkspaceSources$3(Indexer.scala:691)
	scala.meta.internal.metals.Indexer.$anonfun$reindexWorkspaceSources$3$adapted(Indexer.scala:688)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:630)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:628)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1313)
	scala.meta.internal.metals.Indexer.reindexWorkspaceSources(Indexer.scala:688)
	scala.meta.internal.metals.MetalsLspService.$anonfun$onChange$2(MetalsLspService.scala:936)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
	scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
	java.base/java.lang.Thread.run(Thread.java:1583)
```
#### Short summary: 

QDox parse error in file:///D:/ander/Documents/SEMESTRE%207/ARSW/LAB04%20-%20P1%20-%20BLUEPRINTS/Lab_P1_BluePrints_Java21_API/src/main/java/edu/eci/arsw/blueprints/dto/BlueprintDTO.java