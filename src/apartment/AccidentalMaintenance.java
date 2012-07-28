/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package apartment;

import java.sql.Date;

/**
 *
 * @author Thaichau
 */
public class AccidentalMaintenance extends Maintenance{
    String problem;
    java.sql.Date feedbackDate;
    Double rating;

    public AccidentalMaintenance() {
    }

    public Date getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(Date feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
       
}
