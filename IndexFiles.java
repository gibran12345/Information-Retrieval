import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.en.*;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.analysis.CharArraySet;


public class IndexFiles {
  
  private IndexFiles() {}

  /** Index all text files under a directory. */
  public static void main(String[] args) {

    boolean create = true;
    String indexPath = "C:\\Users\\Malbolge\\Desktop\\CF\\results\\lm\\index\\";
//  String indexPath = "C:\\Users\\Malbolge\\Desktop\\CF\\results\\bm25\\index\\";
//	String indexPath = "C:\\Users\\Malbolge\\Desktop\\CF\\results\\vsm\\index";
	String docsPath = "C:\\Users\\Malbolge\\Desktop\\CF\\documents";
	final Path docDir = Paths.get(docsPath);
	
    Date start = new Date();
    try {
      System.out.println("Indexing to directory '" + indexPath + "'...");

      Directory dir = FSDirectory.open(Paths.get(indexPath));
      
//      Analyzer analyzer = new StandardAnalyzer();
//      CharArraySet stopwords = new CharArraySet(0, true);
      Analyzer analyzer = new EnglishAnalyzer();
      IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
      	
//      iwc.setSimilarity(new LMDirichletSimilarity());
      iwc.setSimilarity(new BM25Similarity());
//      iwc.setSimilarity(new ClassicSimilarity());
      
      System.out.println(iwc.getSimilarity());

      if (create) {
        // Create a new index in the directory, removing any
        // previously indexed documents:
        iwc.setOpenMode(OpenMode.CREATE);
      } else {
        // Add new documents to an existing index:
        iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
      }

      IndexWriter writer = new IndexWriter(dir, iwc);
      indexDocs(writer, docDir);

      writer.close();

      Date end = new Date();
      System.out.println(end.getTime() - start.getTime() + " total milliseconds");

    } catch (IOException e) {
      System.out.println(" caught a " + e.getClass() +
       "\n with message: " + e.getMessage());
    }
}

static void indexDocs(final IndexWriter writer, Path path) throws IOException {
    if (Files.isDirectory(path)) {
      Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
          try {
            indexDoc(writer, file, attrs.lastModifiedTime().toMillis());
          } catch (IOException ignore) {
            // don't index files that can't be read.
          }
          return FileVisitResult.CONTINUE;
        }
      });
    } else {
      indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis());
    }
  }

 static void indexDoc(IndexWriter writer, Path file, long lastModified) throws IOException {
   try (InputStream stream = Files.newInputStream(file)) {
      // make a new, empty document
      Document doc = new Document();
      
      // Add the path of the file as a field named "path".  Use a
      // field that is indexed (i.e. searchable), but don't tokenize 
      // the field into separate words and don't index term frequency
      // or positional information:
      Field pathField = new StringField("path", file.toString(), Field.Store.YES);
      
      doc.add(pathField);
      doc.add(new LongPoint("modified", lastModified));
      
      // Add the contents of the file to a field named "contents".  Specify a Reader,
      // so that the text of the file is tokenized and indexed, but not stored.
      // Note that FileReader expects the file to be in UTF-8 encoding.
      // If that's not the case searching for special characters will fail.
      doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))));
      
      if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
       // New index, so we just add the document (no old document can be there):
        System.out.println("adding " + file);
        System.out.println(doc.toString());
        writer.addDocument(doc);
      } else {
        // Existing index (an old copy of this document may have been indexed) so 
        // we use updateDocument instead to replace the old one matching the exact 
        // path, if present:
        System.out.println("updating " + file);
        writer.updateDocument(new Term("path", file.toString()), doc);
      }
    }
  }
}