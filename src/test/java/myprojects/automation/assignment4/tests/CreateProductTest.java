package myprojects.automation.assignment4.tests;

import myprojects.automation.assignment4.BaseTest;
import myprojects.automation.assignment4.model.ProductData;
import org.testng.annotations.Test;

public class CreateProductTest extends BaseTest {
    ProductData Product = ProductData.generate();

    @Test(dataProvider = "loginDate")
    public void createNewProduct(String Username, String Password) {
        actions.openAdminPage();

        actions.login(Username, Password);

        actions.goToPageCreateNewProduct();

        actions.fillSpecifOfNewProduct(Product);

        actions.activateAndSaveNewProduct(Product);

        //actions.logout();

    }
    @Test(dependsOnMethods = "createNewProduct")
    public void checkAddedProduct(){
        actions.openShopPage();

        actions.searchProduct(Product.getName());

        actions.checkSpecOfAddedPrdduct(Product);

    }

}
