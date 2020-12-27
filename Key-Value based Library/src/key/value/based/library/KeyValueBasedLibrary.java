/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package key.value.based.library;
import java.lang.*;
import java.util.HashMap;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;  
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Rahul
 */
class Record
{
    String key;
    String data;
    Long time_to_live;
    Record(String key,String data, Long time_to_live)
    {   this.key=key;
        this.data=data;
        this.time_to_live=time_to_live;
    }
    public String getData()
    {
        return this.data;
    }
    public Long getTimeToLive()
    {
        return this.time_to_live;
    }
    public String getKey()
    {
        return this.key;
    }
}
  class Operations    
{
    
    private HashMap<String,Record> map;
   
      Operations()
      {
  map  =new HashMap<String,Record>();
     }
   
     
    public  boolean HasValue(String key)
    {
        if(map.containsKey(key))
        {
          return true;
        }else
        {
          return false;
        }
    }//For check value exixt or not
    
    public void insertVal(String key, String value,Long time) // Insert Key value pair
    {
        if(!HasValue(key))
        {
            if(map.size()< 1024*1024*1024 && value.length()<=16*1024 ) //Checks that whether the file size is less than 1 GB and also checks that whether the size of value is less than 16kb.
				{
					Long timestamp=0L;
					if(time!=0) 
					timestamp=System.currentTimeMillis()/1000+time;
					if(key.length()<=32)   //To check if key is always a string -capped at 32chars.
					{
					
					map.put(key,new Record(key,value,timestamp));
                                        System.out.println("Success: Inserted");

					}
				}
				else
				{
					System.out.println("Warning: Memory limit exceeded:File should be less than 1 GB and value should be less than 16KB");
				}
        }else
        {
            System.out.println("Warning : Key already exist");
        }
        
				
    }
    
    public void Display(String key) //Display value of key
    {
        if(HasValue(key))
        {
				Long timestamp2=System.currentTimeMillis()/1000;
				Record e= map.get(key);
				
				if(e.time_to_live!=0 )
				{
				if(timestamp2<e.time_to_live)   //Checks current time with the time assigned to the object.
				{
				String s= key+":"  + e.data;
				System.out.println(s);
				}
				else                   //If the time to live has been exceeded then it will show error.
				{
					System.out.println("The time to live of "+key+" has ben expired");
				}
				}
				else                //If no time stamp was given by the user then it simply read the data
				{
					String s=  key+":"  + e.data;
					System.out.println(s);
				}
			
			
        }
        else
        {
             System.out.println("Warning : Key doesnot exist");
        }
    }
    public void delete(String key) //Perform delete operation
    {
         if(HasValue(key))
        {
          
				Long timestamp=System.currentTimeMillis()/1000;
				Record r=map.get(key);
			
				if(r.time_to_live!=0 )
				{
				if(timestamp<r.time_to_live)      //Checks current time with the time assigned to the object.
				{
					map.remove(key);
					System.out.println("Success : Key deleted successfully");
				}
				else                           //If the time to live has been exceeded then it will show error.
				{
					System.out.println("The time to live of "+key+" has ben expired");
				}
				}
				else                     //If no time stamp was given by the user then it simply delete the data
				{
					map.remove(key);
					System.out.println("Success :Key deleted successfully");
				}
        
        }
        else
        {
             System.out.println("Error : can'nt delete Key doesnot exist");
        }
    }
 public void displayAll()
 {
     for (Map.Entry<String, Record> r : map.entrySet())  //Display all the values present in hashmap.
	            System.out.println(r.getKey()+ " : " + r.getValue().getData());   
		
 }
}
class Myprocess extends Thread
{
    public void run()
    {
        Operations op=new Operations();
        while(true)
        {
            String key;
           
           Scanner scan=new Scanner(System.in);
           System.out.println("Enter 1 for  write");
           System.out.println("Enter 2 for  read");
           System.out.println("Enter 3 for delete");
           System.out.println("Enter 4 for display all records");
           System.out.println("Enter 5 for Exit");
           int i=scan.nextInt();
           int flag=1;
           try
           {
           switch(i)
                   {
               case 1:
                   
                   System.out.println("Enter Key,Data and time to live you want to insert : ");
                   key=scan.next();
                   String value=scan.next();
                   Long time=scan.nextLong();
                   op.insertVal(key, value, time);
                   break;
               
               case 2:
                   System.out.println("Enter Key of record you want to get value : ");
                   key=scan.next();
                   op.Display(key);
                   break;
               
               case 3:
                   System.out.println("Enter Key of record you want to delete value : ");
                   key=scan.next();
                   op.delete(key);
                   break;
               case 4:
                   op.displayAll();
                   break;
               case 5:
                   flag=0;
                   break;
       
               default:
                   System.out.println("Error:Wrong Input");
               
                   }
           }catch(Exception e)
           {
               System.out.println("Error:Please try again");
           }
            if(flag==0)
            {
                System.out.println("Bye... Thanks");
                break;
            }
          
            
        }
        
    }
            
    
    
    
}
public class KeyValueBasedLibrary {

    public static void main(String[] args) {
        // TODO code application logic here
        
        Myprocess mt=new Myprocess();
        mt.start();
       
    }
    
    
    
    
    
}
