package de.fhmuenster.bigdata;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

import de.fhmuenster.bigdata.TwitterMatchCount.TwitterMatchCountMapper;
import de.fhmuenster.bigdata.TwitterMatchCount.TwitterMatchCountReducer;

public class TwitterMatchCountTest {
               
                /*We declare three variables for Mapper Driver , Reducer Driver , MapReduceDrivers
                Generics parameters for each of them is point worth noting
                MapDriver generics matches with our test Mapper generics

                SMSCDRMapper extends Mapper<LongWritable, Text, Text, IntWritable>
                Similarly for ReduceDriver we have same matching generics declaration with

                SMSCDRReducer extends Reducer<Text, IntWritable, Text, IntWritable>*/
               
   MapReduceDriver<LongWritable, Text, Text, LongWritable, Text, LongWritable> mapReduceDriver;
   MapDriver<LongWritable, Text, Text, LongWritable> mapDriver;
   ReduceDriver<Text, LongWritable, Text, LongWritable> reduceDriver;

  
   //create instances of our Mapper , Reducer .
   //Set the corresponding mappers and reducers using setXXX() methods
   @Before
   public void setUp() {
	  TwitterMatchCountMapper mapper = new TwitterMatchCount.TwitterMatchCountMapper();
	  TwitterMatchCountReducer reducer = new TwitterMatchCount.TwitterMatchCountReducer();
      mapDriver = new MapDriver<LongWritable, Text, Text, LongWritable>();
      mapDriver.setMapper(mapper);
      reduceDriver = new ReduceDriver<Text, LongWritable, Text, LongWritable>();
      reduceDriver.setReducer(reducer);
      mapReduceDriver = new MapReduceDriver<LongWritable, Text, Text, LongWritable, Text, LongWritable>();
      mapReduceDriver.setMapper(mapper);
      mapReduceDriver.setReducer(reducer);
   }

 @Test
   public void testMapReduce()  throws Exception {
      /*mapReduceDriver.withInput(new LongWritable(1), new Text("sky \n sky \n sky"));
      mapReduceDriver.addOutput(new Text("sky"), new LongWritable(3));
   
      mapReduceDriver.runTest();*/
	 
     StringBuilder sb = new StringBuilder();
	 try {
			BufferedReader in = new BufferedReader(new FileReader("test.txt"));
			String zeile = null;
			while ((zeile = in.readLine()) != null) {
				//sb.append(zeile + "\n");
				mapReduceDriver.withInput(new LongWritable(1), new Text(zeile));
			}
		} catch (IOException e) {
			e.printStackTrace();
	} 
	System.out.println("START:");
    mapReduceDriver.run();
    
 }}