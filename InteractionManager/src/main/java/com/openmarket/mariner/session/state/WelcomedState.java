package com.openmarket.mariner.session.state;

import com.openmarket.mariner.journeys.Journey;
import com.openmarket.mariner.journeys.JourneyService;
import com.openmarket.mariner.session.event.DisruptionEvent;
import com.openmarket.mariner.session.event.ResponseEvent;
import com.openmarket.mariner.session.event.TapOffEvent;
import com.openmarket.mariner.session.event.TapOnEvent;
import com.openmarket.mariner.sms.SmsSender;

public class WelcomedState implements SessionState {

    private SmsSender smsSender;
    private JourneyService journeyService;
    private String origin;

    public WelcomedState(SmsSender smsSender, JourneyService journeyService, String origin) {
        this.smsSender = smsSender;
        this.journeyService = journeyService;
        this.origin = origin;
    }

    @Override
    public SessionState handleTapOn(TapOnEvent event) {
        return this;
    }

    @Override
    public SessionState handleTapOff(TapOffEvent event) {
        return null;
    }

    @Override
    public SessionState handleDisruption(DisruptionEvent event) {
        return this;
    }

    @Override
    public SessionState handleResponse(ResponseEvent event) {
        // Process the response by searching
        Journey journey = journeyService.getJourney(origin, event.getMessage(), false);

        // Send the search results
        smsSender.send(journey.toString() + " - Tess", event.getPhoneNumber());

        // Switch to the Travelling state
        return new TravellingState(smsSender, journeyService, journey);
    }
}
