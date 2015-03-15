package tester;

import Final.fuzzyStringMatcher;

/**
 *
 * @author Robert Walker
 */
public class testDebug {

    public static void main(String[] args) {
        
        System.out.println( fuzzyStringMatcher.fuzzyMatch("TEST", "TEST") );
        System.out.println( fuzzyStringMatcher.fuzzyMatch("ROAD", "ROws") );
        System.out.println( fuzzyStringMatcher.fuzzyMatch("Meet Mitch McGary, The 6'10\" Michigan Freshmen Who Can Ride A Unicycle And Wants To Be A Financial Advisor http://t.co/QQwF4EHO9T", "Meet Mitch McGary, The 6'10\" Michigan Freshmen Who Can Ride A Unicycle And Wants To Be A Financial Advisor http://t.co/YzPMSwfAtD") );
        System.out.println( fuzzyStringMatcher.fuzzyMatch("Metta World Peace", "metta world peace") );
        
    }
}
