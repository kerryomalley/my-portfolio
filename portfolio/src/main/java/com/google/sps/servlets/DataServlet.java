// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList; 
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  int maxNum = 5;
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    ArrayList<String> storedComments = new ArrayList<String>();
    for(Entity entity : results.asIterable()) {
	    String comment  = (String) entity.getProperty("comment");
	    long timestamp = (long) entity.getProperty("timestamp");
	    
	    if(comment == ""){
		continue;
   	    }
	    storedComments.add(comment);
    }
    ArrayList<String> limitedComments = new ArrayList<String>();
    for(int counter = 0; (counter <  maxNum)  && (counter < storedComments.size()); counter++)
    {
    	limitedComments.add(storedComments.get(counter));
    }
    String json = convertToJson(limitedComments);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String text = getParameter(request, "comment", "");
    long timestamp = System.currentTimeMillis();
    maxNum = getUserMaxNum(request);

    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("comment", text);
    commentEntity.setProperty("timestamp", timestamp);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity); 

    response.sendRedirect("/index.html");
  }

  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if(value == null) {
	    return defaultValue;
    }
    return value;
  }


  private String convertToJson(ArrayList<String> list) {
    Gson gson = new Gson();
    String json = gson.toJson(list);
    return json;
  }

  private int getUserMaxNum(HttpServletRequest request) {
    String userMaxNumString = request.getParameter("maxNum");

    int maxNum;
    try {
	    maxNum = Integer.parseInt(userMaxNumString);
    } catch (NumberFormatException e) {
	    System.err.println("Input not a number: " + userMaxNumString);
	    return -1;
    }
    return maxNum; 
  }
}
