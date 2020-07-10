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

package com.google.sps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.io.*;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
	  long duration = request.getDuration();
	  int time = TimeRange.START_OF_DAY;
	  
	  if(duration > (24*60)) {
		return Arrays.asList();
	  }

	  Collection<String> attendees = request.getAttendees();
	  Collection<TimeRange> busyTimes = getBusyTimes(events, attendees);
	  Collection<String> optionalAttendees = request.getOptionalAttendees();
	  Collection<TimeRange> optionalBusyTimes = getBusyTimes(events, optionalAttendees);

	  if(attendees.isEmpty()) {
		Collection<TimeRange> timeSlots = findSlots(optionalBusyTimes, duration);
		return timeSlots; 
  	  }

	  Collection<TimeRange> mandatoryTimeSlots = findSlots(busyTimes, duration);

	  if(optionalAttendees.isEmpty()) {
		return mandatoryTimeSlots;
	  }
	  
	  Collection<TimeRange> timeSlots = new ArrayList<>();
	  for(TimeRange slot : mandatoryTimeSlots) {
		for(TimeRange optionalBusy : optionalBusyTimes) {
			if(slot.overlaps(optionalBusy)) {
				continue;
			}
			timeSlots.add(slot);
		}
	  }

	  if(timeSlots.isEmpty() && !(attendees.isEmpty())) {
		return mandatoryTimeSlots;
	  }
	  return timeSlots;
	 


  }


  public Collection<TimeRange> findSlots(Collection<TimeRange> busyTimes, long duration) {
	Collection<TimeRange> timeSlots =  new ArrayList<>();
	int time = TimeRange.START_OF_DAY;
	for(TimeRange t: busyTimes) {
		int startTime = t.start();
		int endTime = t.end();
		if(endTime <= time) {
			continue;
		}
		TimeRange availableSlot = TimeRange.fromStartEnd(time, startTime, false);
		time = endTime;
		int availableDuration = availableSlot.end() - availableSlot.start();
		if(availableDuration >= duration) {
			timeSlots.add(availableSlot);
		}
	}
	if((time < TimeRange.END_OF_DAY) && ((TimeRange.END_OF_DAY - time) >= duration)){
		TimeRange availableSlot = TimeRange.fromStartEnd(time, TimeRange.END_OF_DAY, true);
		timeSlots.add(availableSlot);
	}
	return timeSlots; 
  }
  

  public Collection<TimeRange> getBusyTimes(Collection<Event> events, Collection<String> attendees) {
	Collection<TimeRange> busyTimes = new ArrayList<>();

	for(Event e: events) {
		boolean attendeeOverlap = false;
		for(String person: attendees) {
			if(e.getAttendees().contains(person)){
				attendeeOverlap = true;
			}
		}
		if(attendeeOverlap) {
			busyTimes.add(e.getWhen());
		}
	}

	Collections.sort((ArrayList) busyTimes, TimeRange.ORDER_BY_START);
	
	return busyTimes;
  }
}
