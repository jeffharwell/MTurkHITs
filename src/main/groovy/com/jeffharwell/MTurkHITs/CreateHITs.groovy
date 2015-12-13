package com.jeffharwell.MTurkHITs

import com.amazonaws.mturk.service.axis.RequesterService;
import com.amazonaws.mturk.service.exception.ServiceException;
import com.amazonaws.mturk.util.PropertiesClientConfig;
import com.amazonaws.mturk.requester.HIT;
import com.amazonaws.mturk.addon.HITQuestion;
import com.amazonaws.mturk.requester.Comparator;
import com.amazonaws.mturk.requester.Locale;
import com.amazonaws.mturk.requester.QualificationRequirement;
//import org.apache.log4j.Category;
//import org.apache.log4j.BasicConfigurator;

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

        // This is an example of creating a qualification.
        // This is a built-in qualification -- user must be based in the US
        QualificationRequirement qualReq = new QualificationRequirement();
        qualReq.setQualificationTypeId(RequesterService.LOCALE_QUALIFICATION_TYPE_ID);
        qualReq.setComparator(Comparator.EqualTo);
        Locale country = new Locale();
        country.setCountry("US");
        qualReq.setLocaleValue(country);

        // The create HIT method takes in an array of QualificationRequirements
        // since a HIT can have multiple qualifications.
        QualificationRequirement[] qualReqs = [ qualReq ] as QualificationRequirement[]
        // qualReqs = new QualificationRequirement[] { qualReq };

        HITQuestion question = new HITQuestion("external_question.xml")
        def prop = new ConfigSlurper().parse(new File("question.properties").toURL())
        println prop["title"]
        try {
            HIT hit = service.createHIT(null, // HITTypeId 
                prop["title"],
                prop["description"], prop["keywords"],
                question.getQuestion(),
                prop["reward"].toDouble(), prop["duration_in_seconds"].toLong(),
                prop["auto_approval_delay_in_seconds"].toLong(),
                prop["lifetime_in_seconds"].toLong(),
                prop["number_of_assignments"].toInteger(),
                prop["requester_annotation"],
                qualReqs,
                null // responseGroup
            );

            /*
            HIT hit = service.createHIT(
                title,
                description,
                reward,
                RequesterService.getBasicFreeTextQuestion(
                    "My test question?"),numAssignments);
            */

            println "Created HIT: ${hit.getHITId()}"

            println "You may see your HIT with HITTypeId: ${hit.getHITTypeId()} here"
            println "${service.getWebsiteURL()}/mturk/preview?groupId=${hit.getHITTypeId()}"
        } catch (ServiceException e) {
            println e.getLocalizedMessage()
        }
    }

    public static void main(String[] args) {
        //BasicConfigurator.configure()

        CreateHITs mthit = new CreateHITs()

        if (mthit.hasEnoughFund()) {
            mthit.createHit()
            println "Created HIT"
        } else {
            println "Not enough funds available"
        }
    }
}
