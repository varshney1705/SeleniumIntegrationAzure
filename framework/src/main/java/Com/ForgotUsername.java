package Com;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;


public class ForgotUsername {
    void utSetUp() throws Exception {
    }

    void setUp() throws Exception {

    }

    @DataProvider(name = "data-provider")
    public Object[] dpMethod() {
        // return new Object[][] {{"First-Value"}, {"Second-Value"}};
        Content fileUtil = new Content();
        List<String> emailList = fileUtil.readFile();
        return emailList.toArray();
    }

    @Test(dataProvider = "data-provider")
    void test(String a) throws Exception {
        boolean testPassed = false;


        DriverUtility driverUtility = DriverUtility.getInstance();
        testPassed = driverUtility.performTest(a);
        driverUtility.shutdownDriver();

        if(Content.map.get(a).getExpectedResult().equalsIgnoreCase("pass")){
            Assert.assertEquals(true, testPassed);
        }else{
            Assert.assertEquals(false, testPassed);

        }


    }

    @AfterMethod
    void tearDown() {

    }

}
