package com.jeffharwell.MTurkHITs

import com.amazonaws.mturk.service.axis.RequesterService;
import com.amazonaws.mturk.service.exception.ServiceException;
import com.amazonaws.mturk.util.PropertiesClientConfig;
import com.amazonaws.mturk.requester.HIT;

class CreateHITs {

    private RequesterService service;
    private String title = "Answer another question";
    private String description = "This is another HIT created by my Groovy code!!"
    private int numAssignments = 1
    private double reward = 0.05

    CreateHITs() {
        service = new RequesterService(new PropertiesClientConfig("mturk.properties"));
    }

    boolean hasEnoughFund() {
        def balance = service.getAccountBalance()
        println("Got account balance: ${RequesterService.formatCurrency(balance)}")
        return balance > reward;
    }

    def createHit() {
        try {
            HIT hit = service.createHIT(
                title,
                description,
                reward,
                RequesterService.getBasicFreeTextQuestion(
                    "My test question?"),numAssignments);

            println "Created HIT: ${hit.getHITId()}"

            println "You may see your HIT with HITTypeId: ${hit.getHITTypeId()} here"
            println "${service.getWebsiteURL()}/mturk/preview?groupId=${hit.getHITTypeId()}"
        } catch (ServiceException e) {
            println e.getLocalizedMessage()
        }
    }

    public static void main(String[] args) {
        CreateHITs mthit = new CreateHITs()

        if (mthit.hasEnoughFund()) {
            mthit.createHit()
            println "Created HIT"
        } else {
            println "Not enough funds available"
        }
    }
}
