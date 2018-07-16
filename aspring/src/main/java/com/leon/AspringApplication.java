package com.leon;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import com.leon.model.Customer;

@SpringBootApplication
public class AspringApplication  implements CommandLineRunner{

	private final static Logger log=LoggerFactory.getLogger(AspringApplication.class);
	@Autowired
	JdbcTemplate jdbcTem;
	
	public static void main(String[] args) {
		SpringApplication.run(AspringApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		String createTable=" IF NOT EXISTS(SELECT [name] FROM sys.tables WHERE[name]='Customers')   CREATE TABLE CUSTOMERS(id int IDENTITY(1,1),first_name VARCHAR(100),last_name VARCHAR(100))";
		String insertIntoTable="Insert into Customers (first_name,last_name) VALUES (?,?)";
		String selectByName =" select id ,first_name,last_name from customers where first_name=?";
		log.info("creating table ");
		jdbcTem.execute(createTable);
		System.out.println("table created");
		List<Object[]> splitUpNames=Arrays.asList("Joh who","John sake","Josh blocj","Josh AD","Leon Strong").stream()
				.map(name-> name.split(" ")).
				collect(Collectors.toList());
				 
		splitUpNames.forEach(name->log.info("Inserting into Table"));		

		jdbcTem.batchUpdate(insertIntoTable,splitUpNames);
		
		log.info("quering for customer whose name Josh");
		
		jdbcTem.query(selectByName,new Object[] {"Josh"},
		(rs,rowNum)->new Customer(rs.getLong("id"),rs.getString("first_name"),rs.getString("last_name"))).forEach(customer-> log.info(customer.toString()));		
		
	}
}
