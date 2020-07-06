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
import com.google.sps.data.Task;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  int maxNum = 5;
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    ArrayList<Task> storedComments = new ArrayList<Task>();
    for(Entity entity : results.asIterable()) {
	    long id = entity.getKey().getId();
	    String comment  = (String) entity.getProperty("comment");
	    long timestamp = (long) entity.getProperty("timestamp");
	    String email = (String) entity.getProperty("useremail");
	    
	    if(comment == ""){
		continue;
   	    }

	    Task task = new Task(id, comment, timestamp, email);
	    storedComments.add(task);
    }
    ArrayList<Task> limitedComments = new ArrayList<Task>();
    for(int counter = 0; (counter <  maxNum)  && (counter < storedComments.size()); counter++)
    {
    	limitedComments.add(storedComments.get(counter));
    }

    Gson gson = new Gson();
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(limitedComments));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String text = getParameter(request, "comment", "");
    long timestamp = System.currentTimeMillis();
    maxNum = getUserMaxNum(request);

    UserService userService = UserServiceFactory.getUserService();
    String email = userService.getCurrentUser().getEmail();

    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("comment", text);
    commentEntity.setProperty("timestamp", timestamp);
    commentEntity.setProperty("useremail", email);

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
