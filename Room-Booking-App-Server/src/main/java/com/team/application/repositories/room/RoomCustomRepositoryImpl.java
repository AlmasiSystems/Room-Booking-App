package com.team.application.repositories.room;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import com.team.application.models.Room;

@Repository
public class RoomCustomRepositoryImpl implements RoomCustomRepository{
	
	    @PersistenceContext
	    private EntityManager entityManager;

	    @SuppressWarnings("unchecked")
		public List<Room> roomQuery(String hc_name, String city, String state, String country, Integer rating, Integer capacity, Double price, Double area, Date start,Date end){

	    	String query = 
	    			"SELECT room.*	from	room,hotel	WHERE ((" + 
	    			"	room.hotel_id=hotel.hotel_id	AND" + 
	    			"	hotel.city=?	AND" + 
	    			"	hotel.state=?	AND" + 
	    			"	hotel.country=?	" +
	    			")									" ;
	    	Object[] parameters = new Object[5];
	    	int paramCounter=0;
	    	if(!(hc_name == null && rating == null && capacity == null && price == null && area == null)) {
	    		query+="	 AND	(";
	    		
		    	if(capacity!=null) {
		    		parameters[paramCounter++]=capacity;
		   			query+="	room.capacity>=?";
		   		}
		    		
		   		if(price!=null) {
		   			if(paramCounter>0) query+= "	AND";
		   			parameters[paramCounter++]=price;
	    			query+="	room.price<=?";
	    		}
		    		
		    	if(area!=null) {
		    		if(paramCounter>0) query+= "	AND";
		   			parameters[paramCounter++]=area;
		   			query+="	room.area>=?";
		   		}
		    		
		    	if(rating!=null) {
		    		if(paramCounter>0) query+= "	AND";
		   			parameters[paramCounter++]=rating;
		   			query+="	hotel.rating>=?";
		   		}
		    	
		    	if(hc_name!=null) {
		    		if(paramCounter>0) query+= "	AND";
		   			parameters[paramCounter++]=hc_name;
		   			query+="	hotel.hc_name=?";
		   		}
		    		
		    	query+="	)";		
	    	} else {
	    		//skip
	    	}
	    	query+=
	    	"	AND ( "+ 
	    	"	NOT EXISTS (" + 
	    	"			SELECT 1 FROM reservation WHERE (" + 
	    	"				(reservation.hotel_id=room.hotel_id AND reservation.room_number=room.room_number ) AND (" + 
	    	"					(reservation.start_date<=? AND reservation.end_date>?) OR" + //-- start
	    	"					(reservation.start_date<? AND reservation.end_date>=?) OR" + //-- end
	    	"					(reservation.start_date>=? AND reservation.end_date<=?)" + //-- ?1 :start , ?2 : end
	    	"				)" + 
	    	"			)" + 
	    	"		) " + 
	    	"	)"+
	    	"	) GROUP BY room.hotel_id,room.room_number ;";
	    	Query nativeQuery = entityManager.createNativeQuery(query,Room.class);
	    	nativeQuery.setParameter(1, city);
	    	nativeQuery.setParameter(2, state);
	    	nativeQuery.setParameter(3, country);
	    	for (int i=0;i<parameters.length && parameters[i]!=null;i++) {
	    		nativeQuery.setParameter(4+i, parameters[i]);
	    	}
	    	paramCounter+=4;
	    	nativeQuery.setParameter(paramCounter++, start);
	    	nativeQuery.setParameter(paramCounter++, start);
	    	nativeQuery.setParameter(paramCounter++, end);
	    	nativeQuery.setParameter(paramCounter++, end);
	    	nativeQuery.setParameter(paramCounter++, start);
	    	nativeQuery.setParameter(paramCounter++, end);
	    	
	    	return (List<Room>) nativeQuery.getResultList();
	    }
	    
	    @SuppressWarnings("unchecked")
	    public List<Room> roomsInHotelQuery(Integer hotel_id, Integer capacity, Double price, Double area, Date start,Date end){

	    	String query = 
	    			"SELECT room.*	from	room,hotel	WHERE ((" + 
	    			"	room.hotel_id=?	)	" ;
	    	Object[] parameters = new Object[3];
	    	int paramCounter=0;
	    	if(!(capacity == null && price == null && area == null)) {
	    		query+="	 AND	(";
	    		
		    	if(capacity!=null) {
		    		parameters[paramCounter++]=capacity;
		   			query+="	room.capacity>=?";
		   		}
		    		
		   		if(price!=null) {
		   			if(paramCounter>0) query+= "	AND";
		   			parameters[paramCounter++]=price;
	    			query+="	room.price<=?";
	    		}
		    		
		    	if(area!=null) {
		    		if(paramCounter>0) query+= "	AND";
		   			parameters[paramCounter++]=area;
		   			query+="	room.area>=?";
		   		}
		    		
		    	query+="	)";		
	    	} else {
	    		//skip
	    	}
	    	query+=
	    	"	AND ( "+ 
	    	"	NOT EXISTS (" + 
	    	"			SELECT 1 FROM reservation WHERE (" + 
	    	"				(reservation.hotel_id=room.hotel_id AND reservation.room_number=room.room_number ) AND (" + 
	    	"					(reservation.start_date<=? AND reservation.end_date>?) OR" + //-- start
	    	"					(reservation.start_date<? AND reservation.end_date>=?) OR" + //-- end
	    	"					(reservation.start_date>=? AND reservation.end_date<=?)" + //-- ?1 :start , ?2 : end
	    	"				)" + 
	    	"			)" + 
	    	"		) " + 
	    	"	)"+
	    	"	) GROUP BY room.hotel_id,room.room_number ;";
	    	Query nativeQuery = entityManager.createNativeQuery(query,Room.class);
	    	nativeQuery.setParameter(1, hotel_id);
	    	for (int i=0;i<parameters.length && parameters[i]!=null;i++) {
	    		nativeQuery.setParameter(2+i, parameters[i]);
	    	}
	    	paramCounter+=2;
	    	nativeQuery.setParameter(paramCounter++, start);
	    	nativeQuery.setParameter(paramCounter++, start);
	    	nativeQuery.setParameter(paramCounter++, end);
	    	nativeQuery.setParameter(paramCounter++, end);
	    	nativeQuery.setParameter(paramCounter++, start);
	    	nativeQuery.setParameter(paramCounter++, end);
	    	
	    	return (List<Room>) nativeQuery.getResultList();
	    }
}
