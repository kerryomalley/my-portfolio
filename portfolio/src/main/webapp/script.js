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
      ['I play on the lacrosse team at my school', 'I have been ziplining 3 times', 'One time I fell into a cactus', 'I have a dog names Atticus Finch'];

  // Pick a random greeting.
  const fact = facts[Math.floor(Math.random() * facts.length)];

  // Add it to the page.
  const factContainer = document.getElementById('fact-container');
  factContainer.innerText = fact;
}

function openPage(evt, pageName){
	var i, innercontent, links;
	innercontent = document.getElementsByClassName("innercontent");
	for(i = 0; i < innercontent.length; i++) {
		innercontent[i].style.display = "none";
	}

	links = document.getElementsByClassName("tabs");
	for(i = 0; i < links.length; i++) {
		links[i].className = links[i].className.replace(" active", "")
	}

	document.getElementById(pageName).style.display = "block";
	evt.currentTarget.className += " active";
	
} 

function getMessage() {
	fetch('/data').then(response => response.json()).then((comments) => {
		const commentSection = document.getElementById('message-container');
		comments.forEach((comment) => {
			commentSection.appendChild(createComment(comment));
		})
	});
}

function createComment(message) {
	const commentElement = document.createElement('li');
	commentElement.className = 'comment';
	
	//const userEmailElement = document.createElememt('span');
	//userEmailElement.innerHTML = message.email;

	const messageElement = document.createElement('span');
	messageElement.innerHTML = (message.useremail + "<br>" + message.comment);
	//messageElement.innerHTML = message.comment;

	const deleteButtonElement = document.createElement('button');
	deleteButtonElement.innerText = 'Delete';
	deleteButtonElement.addEventListener('click', () => {
		deleteData(message);

		commentElement.remove();
	});

	//commentElement.appendChild(userEmailElement);
	commentElement.appendChild(messageElement);
	commentElement.appendChild(deleteButtonElement);
	return commentElement;
}
function deleteData(comment) {
	console.log('Deleting Data');
	const params = new URLSearchParams();
	params.append('id', comment.id);
	fetch('/delete-data', {method: 'POST', body: params});

}

function userLogin() {
	console.log('Fetching login status');
	
	const loginStatus =  fetch("/loginstatus");
	loginStatus.then(response => response.text()).then(message => message.trim()).then(toggleComments);	
}


function toggleComments(isLoggedIn) {
	console.log('Toggling Comments');
	if(isLoggedIn == "true") 
	{
		document.getElementById('comment-section').style.display = 'block';
		fetch('/userlogin').then(response => response.text()).then(addQuoteToDom);
	}
	else
	{
		document.getElementById('comment-section').style.display = 'none';
		fetch('/userlogin').then(response => response.text()).then(addQuoteToDom);
	}
}


function handleResponse(response) {
	console.log('Handling the response');
	
	const textPromise = response.text();

	textPromise.then(toggleComments);
}	

function addQuoteToDom(quote) {
	console.log('Adding quote to dom: ' + quote);

	const quoteContainer = document.getElementById('user-login-content');
	quoteContainer.innerHTML = quote;
}
