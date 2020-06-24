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

function openPage(pageName){
	var i, innercontent, links;
	innercontent = document.getElementsByClassName("innercontent");
	for(i = 0; i < innercontent.length; i++) {
		innercontent[i].style.display = "none";
	}

	links = document.getElementsByClassName("tabs");
	for(i = 0; i < links.length; i++) {
		links[i].style.backgroundColor = "";
	}

	document.getElementById(pageName).style.display = "block";

	elmnt.style.backgroundColor = color; 
}

document.getElementById("defaultOpen").click();
