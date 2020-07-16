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

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const facts  =
      ['I play on the lacrosse team at my school',
	      'I have been ziplining 3 times',
	      'One time I fell into a cactus',
	      'I have a dog names Atticus Finch'];

  // Pick a random greeting.
  const fact = facts[Math.floor(Math.random() * facts.length)];

  // Add it to the page.
  const factContainer = document.getElementById('fact-container');
  factContainer.innerText = fact;
}

/**
 * Opens the tab that has been clicked on 
 */
function openPage(evt, pageName) {
  let i, innercontent, links;
  innercontent = document.getElementsByClassName("innercontent");

  // Make the other tabs invisible 	
  for(i = 0; i < innercontent.length; i++) {
    innercontent[i].style.display = "none";
  }

  links = document.getElementsByClassName("tabs");

  for(i = 0; i < links.length; i++) {
    links[i].className = links[i].className.replace(" active", "")
  }

  // Displays the chosen tab 	
  document.getElementById(pageName).style.display = "block";
  evt.currentTarget.className += " active";	
} 

/**
 * Gets the message the user has input 
 */
function getMessage() {
  fetch('/data').then(response => response.json()).then((comments) => {
    const commentSection = document.getElementById('message-container');
    comments.forEach((comment) => {
      commentSection.appendChild(createComment(comment));
    })
  });
}

/**
 * Creates the HTML for the comment 
 */
function createComment(message) {
  const commentElement = document.createElement('li');
  commentElement.className = 'comment';

  // Adds the users email and their comment to the message span	
  const messageElement = document.createElement('span');
  messageElement.innerHTML = (message.userEmail + "<br>" + message.comment);

  // Adds a delete button that when clicked deletes the comment 
  const deleteButtonElement = document.createElement('button');
  deleteButtonElement.innerText = 'Delete';
  deleteButtonElement.addEventListener('click', () => {
    deleteData(message);
    commentElement.remove();
  });

  //Adds all the componenets created  to an overall comment element 
  commentElement.appendChild(messageElement);
  commentElement.appendChild(deleteButtonElement);
  return commentElement;
}

/**
 * Deletes the data when a comment is deleted
 */
function deleteData(comment) { 
  const params = new URLSearchParams();
  params.append('id', comment.id);
  fetch('/delete-data', {method: 'POST', body: params});
}

/**
 * Retrieves the login status of the user 
 */
function userLogin() {
  const loginStatus =  fetch("/loginstatus");
  loginStatus.then(response => response.text()).then(message => 
	  message.trim()).then(toggleComments);	
}

/**
 * Displays the comments or makes them invisible depending on the login status
 */
function toggleComments(isLoggedIn) {
  // If the person is logged in, displays the comments and displays their status
  if(isLoggedIn == "true") {
    document.getElementById('comment-section').style.display = 'block';
    fetch('/userlogin').then(response => response.text()).then(addQuoteToDom);
  } else { // If the person is logged out, hides comments 
    document.getElementById('comment-section').style.display = 'none';
    fetch('/userlogin').then(response => response.text()).then(addQuoteToDom);
  }
}

function handleResponse(response) {
  const textPromise = response.text();
  textPromise.then(toggleComments);
}	

/**
 * Adds the given quote to the webpage 
 */
function addQuoteToDom(quote){
  const quoteContainer = document.getElementById('user-login-content');
  quoteContainer.innerHTML = quote;
}

/**
 * Creates a map of Hyde Park with markers of restaurants 
 */
function createMap() {
  // Create map element centered in Hyde Park 
  const map = new google.maps.Map(
    document.getElementById('map'),
    {center: {lat: 41.793, lng: -87.594}, zoom: 16});

  // Add marker of Medici onto map
  const medMarker = new google.maps.Marker({
    position: {lat: 41.7915, lng: -87.594}, 
    map: map, 
    title: 'Medici on 57th'
  });

  // Add marker of Shinjou onto map
  const shinMarker = new google.maps.Marker({
    position: {lat: 41.799, lng: -87.592},
    map: map,
    title: 'Shinjou'
  });

  // Add marker of Noodles Ect. onto map 
  const noodlesMarker = new google.maps.Marker({
    position: {lat: 41.7913,lng: -87.593},
    map: map,
    title: 'Noodles Etc.'
  });
}
