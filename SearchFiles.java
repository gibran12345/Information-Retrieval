import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.analysis.CharArraySet;

public class SearchFiles {

  private SearchFiles() {}

  public static void main(String[] args) throws Exception {
	
	String qresultpath = "C:\\Users\\Malbolge\\Desktop\\CF\\results\\lm\\queriedocs\\";
//	String qresultpath = "C:\\Users\\Malbolge\\Desktop\\CF\\results\\bm25\\queriedocs\\";
//	String qresultpath = "C:\\Users\\Malbolge\\Desktop\\CF\\results\\vsm\\queriedocs\\";
//	String qresultpath = "C:\\Users\\Malbolge\\Desktop\\CF\\queriedocs\\";
	String qpath = "C:\\Users\\Malbolge\\Desktop\\CF\\queries\\";
	String index = "C:\\Users\\Malbolge\\Desktop\\CF\\results\\lm\\index";
//	String index = "C:\\Users\\Malbolge\\Desktop\\CF\\results\\bm25\\index";
//	String index = "C:\\Users\\Malbolge\\Desktop\\CF\\results\\vsm\\index";

    String field = "contents";
    int hitsPerPage = 100;

    IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
    IndexSearcher searcher = new IndexSearcher(reader);
    
    // Using BM25 to calculate the similarity
//    searcher.setSimilarity(new BM25Similarity());
//    searcher.setSimilarity(new ClassicSimilarity());
    searcher.setSimilarity(new LMDirichletSimilarity());
    
//     Analyzer analyzer = new StandardAnalyzer();
    // Using the English analyzer to parse the text in the file
    
//    CharArraySet stopwords = new CharArraySet(0, true);
    Analyzer analyzer = new EnglishAnalyzer();

    final File folder = new File(qpath);
    
    List<String> result = new ArrayList<>();
    
    for (final File f: folder.listFiles())
    	if (f.isFile())
    		if (f.getName().matches(".*\\.txt"))
    			result.add(f.getAbsolutePath());
    
    for (String s: result)
    {
    	
    	File myFile = new File(s);
    	Scanner myReader = new Scanner(myFile);
   
    	String queryString = myReader.nextLine();
    	
    	Query query = new QueryParser(field, analyzer).parse(queryString.trim());
    	System.out.println("Searching for: " + query.toString(field));
    
    	doSearch(searcher, query, hitsPerPage, qresultpath + getFileNumber(s) + ".txt");
    	
    	myReader.close();
    	
    }
    
    reader.close();
  
  }

  public static void doSearch(IndexSearcher searcher, Query query, 
                             int hitsPerPage, String docname) throws IOException 
 {
 
    TopDocs results = searcher.search(query, hitsPerPage);
    
    System.out.println(searcher.getSimilarity());
    ScoreDoc[] hits = results.scoreDocs;
    
    int numTotalHits = Math.toIntExact(results.totalHits.value);
    System.out.println(numTotalHits + " total matching documents");

    int start = 0;
    int end = Math.min(numTotalHits, hitsPerPage);

    end = Math.min(hits.length, start + hitsPerPage);
        
    System.out.println(docname);
    File docq = new File(docname);
    docq.createNewFile();		
    
    FileWriter myWriter = new FileWriter(docname);
        
    for (int i = start; i < end; i++) 
    {

	    Document doc = searcher.doc(hits[i].doc);
	    String path = doc.get("path");
	    
	    if (path != null) 
	    {
	    	
	    	System.out.println((i + 1) + ". " + path);
	    	myWriter.write(getFileNumber(path) + ' ');

	    }
    }
    
    myWriter.close();
    
  }
  
  public static String getFileNumber(String s)
  {
	  
	  String[] output = s.split("\\\\");
	  String[] output2 = output[output.length - 1].split("[.]");
	  
	  return output2[0];
	  
  }
  
}