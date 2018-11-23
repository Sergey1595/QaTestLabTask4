package myprojects.automation.assignment4;


import myprojects.automation.assignment4.model.ProductData;
import myprojects.automation.assignment4.utils.Properties;
import myprojects.automation.assignment4.utils.logging.CustomReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Contains main script actions that may be used in scripts.
 */
public class GeneralActions {
    private WebDriver Driver;
    private WebDriverWait Wait;
    private JavascriptExecutor Js;
    public GeneralActions(WebDriver driver) {
        this.Driver = driver;
        Wait = new WebDriverWait(driver, 30);
        Js = (JavascriptExecutor)Driver;
    }

    /**
     * Logs in to Admin Panel.
     * param login
     * param password
     */
    public void openAdminPage(){
        Driver.get(Properties.getBaseAdminUrl());
    }

    public void openShopPage(){
        Driver.get(Properties.getBaseUrl());
    }

    public void login(String Login, String Password) {
        Wait.until(ExpectedConditions.presenceOfElementLocated(By.id("email")));
        WebElement FieldEmail = Driver.findElement(By.id("email"));

        Wait.until(ExpectedConditions.presenceOfElementLocated(By.id("passwd")));
        WebElement FieldPass = Driver.findElement(By.id("passwd"));

        FieldEmail.sendKeys(Login);
        FieldPass.sendKeys(Password);

        Wait.until(ExpectedConditions.textToBePresentInElementValue(By.id("email"), Login));
        Wait.until(ExpectedConditions.textToBePresentInElementValue(By.id("passwd"), Password));

        Wait.until(ExpectedConditions.and(
                ExpectedConditions.presenceOfElementLocated(By.name("submitLogin")),
                ExpectedConditions.elementToBeClickable(By.name("submitLogin")))
        );
        WebElement ButtonSignIn = Driver.findElement(By.name("submitLogin"));
        ButtonSignIn.submit();
        CustomReporter.logAction("Login completed");

    }

    public void logout(){
        Wait.until(ExpectedConditions.presenceOfElementLocated(By.id("header_employee_box")));
        WebElement HeaderEmplBox = Driver.findElement(By.id("header_employee_box"));
        HeaderEmplBox.click();

        Wait.until(ExpectedConditions.presenceOfElementLocated(By.id("header_logout")));
        WebElement ButtonExit = Driver.findElement(By.id("header_logout"));
        Wait.until(ExpectedConditions.visibilityOf(ButtonExit));
        ButtonExit.click();
    }

    public void createProduct(ProductData newProduct) {
        //find button "Catalog"
        Wait.until(ExpectedConditions.presenceOfElementLocated(By.id("subtab-AdminCatalog")));
        WebElement ButtonCatalog = Driver.findElement(By.id("subtab-AdminCatalog"));
        Wait.until(ExpectedConditions.elementToBeClickable(ButtonCatalog));

        //find button "Products"
        Wait.until(ExpectedConditions.presenceOfElementLocated(By.id("subtab-AdminProducts")));
        WebElement ButtonProducts = Driver.findElement(By.id("subtab-AdminProducts"));


        //press to button "Products"
        try{
            Actions MoveToBtnCategory = new Actions(Driver);
            MoveToBtnCategory.moveToElement(ButtonCatalog).pause(Duration.ofSeconds(5)).build().perform();
            Actions PressToBtnProduct = new Actions(Driver);
            PressToBtnProduct.click(ButtonProducts).build().perform();
        }//This is for Firefox
        catch (WebDriverException e ){
            ButtonCatalog.click();
        }

        //press to button "Add new product"
        WebElement ButtonAddProduct = null;
        try{
            Wait.until(ExpectedConditions.presenceOfElementLocated(By.id("page-header-desc-configuration-add")));
            ButtonAddProduct = Driver.findElement(By.id("page-header-desc-configuration-add"));
        }catch (TimeoutException ex){
            //this is for IE
            try{
                ButtonCatalog.click();
                Wait.until(ExpectedConditions.presenceOfElementLocated(By.id("page-header-desc-configuration-add")));
                ButtonAddProduct = Driver.findElement(By.id("page-header-desc-configuration-add"));
            }catch (TimeoutException e){
                Js.executeScript("arguments[0].click();", ButtonCatalog);
                Wait.until(ExpectedConditions.presenceOfElementLocated(By.id("page-header-desc-configuration-add")));
                ButtonAddProduct = Driver.findElement(By.id("page-header-desc-configuration-add"));
            }
        }

        Actions ClickButtonAddProduct = new Actions(Driver);
        ClickButtonAddProduct.moveToElement(ButtonAddProduct).pause(Duration.ofSeconds(5)).click(ButtonAddProduct).build().perform();

        //write name of new product
        Wait.until(ExpectedConditions.presenceOfElementLocated(By.id("form_step1_name_1")));
        WebElement FieldNameOfNewProduct  = Driver.findElement(By.id("form_step1_name_1"));
        Wait.until(ExpectedConditions.visibilityOf(FieldNameOfNewProduct));
        FieldNameOfNewProduct.sendKeys(newProduct.getName());

        //write quantity of new product
        Wait.until(ExpectedConditions.presenceOfElementLocated(By.id("form_step1_qty_0_shortcut")));
        WebElement FieldQuantityOfNewProduct  = Driver.findElement(By.id("form_step1_qty_0_shortcut"));
        Wait.until(ExpectedConditions.visibilityOf(FieldQuantityOfNewProduct));
        String Quantity = "document.getElementById('form_step1_qty_0_shortcut').value=" + newProduct.getQty();
        Js.executeScript(Quantity);

        //write price of new product
        Wait.until(ExpectedConditions.presenceOfElementLocated(By.id("form_step1_price_shortcut")));
        WebElement FieldpriceOfNewProduct  = Driver.findElement(By.id("form_step1_price_shortcut"));
        Wait.until(ExpectedConditions.visibilityOf(FieldpriceOfNewProduct));
        String Price = "document.getElementById('form_step1_price_shortcut').value=" + newProduct.getPrice();
        Js.executeScript(Price);

        //use switch to activate new product
        Wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='switch-input ']")));
        WebElement SwitchOnlineNewProduct = Driver.findElement(By.xpath("//div[@class='switch-input ']"));
        SwitchOnlineNewProduct.click();

        //check alert of activate new product and close alert
        try{
            Wait.until(ExpectedConditions.and(
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='growl-close']")),
                    ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='growl-close']"))
            ));
            WebElement ButtonCloseSucsessAlert = Driver.findElement(By.xpath("//div[@class='growl-close']"));
            Actions ClickButtonCloseAlert = new Actions(Driver);
            ClickButtonCloseAlert.moveToElement(ButtonCloseSucsessAlert).click().build().perform();
            CustomReporter.logAction("New product was activate");
        } catch (TimeoutException e){
            Assert.assertTrue(false, "New product was`nt activate");
        }


        //press to save new product button
        Wait.until(ExpectedConditions.presenceOfElementLocated(By.id("submit")));
        WebElement ButtonSaveNewProduct = Driver.findElement(By.id("submit"));
        Wait.until(ExpectedConditions.elementToBeClickable(ButtonSaveNewProduct));
        ButtonSaveNewProduct.click();

        //check alert of save new product and close alert
        try{
            Wait.until(ExpectedConditions.and(
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='growl-close']")),
                    ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='growl-close']"))
            ));
            WebElement ButtonCloseSucsessAlert = Driver.findElement(By.xpath("//div[@class='growl-close']"));
            Actions ClickButtonCloseAlert = new Actions(Driver);
            ClickButtonCloseAlert.moveToElement(ButtonCloseSucsessAlert).click().build().perform();
            CustomReporter.logAction("New product was save");
        } catch (TimeoutException e){
            Assert.assertTrue(false, "New product was`nt save");
        }

        CustomReporter.logAction("Product was add. Name of product: " + newProduct.getName() + ". Price: " + newProduct.getPrice() + ". Quantity: " + newProduct.getQty());
    }

    public void checkAddedProduct(ProductData Product){
        //find button allProducts
        Wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@class='all-product-link pull-xs-left pull-md-right h4']")));
        WebElement btnAllProducts =  Driver.findElement(By.xpath("//a[@class='all-product-link pull-xs-left pull-md-right h4']"));
        Wait.until(ExpectedConditions.elementToBeClickable(btnAllProducts));
        String PressToBtnAllProducts  = "document.getElementsByClassName('all-product-link pull-xs-left pull-md-right h4')[0].click()";
        Js.executeScript(PressToBtnAllProducts);


        //search added product on all pages
        exit: for(;true;){
            Wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='products row']")));
            Wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[@itemprop='name']/a")));
            List<WebElement> FoundedProducts = Driver.findElements(By.xpath("//h1[@itemprop='name']/a"));
            Wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='col-md-6 hidden-sm-down total-products']/p")));
            Assert.assertTrue(FoundedProducts.size() > 0, "Page without products");
            CustomReporter.logAction("Found " + FoundedProducts.size() + " products.");

            //Search product by name on one page of product by name
            for(int j = 0; j < FoundedProducts.size(); j++){
                FoundedProducts = Driver.findElements(By.xpath("//h1[@itemprop='name']/a"));
                CustomReporter.logAction("Found name is " + FoundedProducts.get(j).getText());
                if(FoundedProducts.get(j).getText().equalsIgnoreCase(Product.getName())){
                    FoundedProducts = Driver.findElements(By.xpath("//h1[@itemprop='name']/a"));
                    Wait.until(ExpectedConditions.elementToBeClickable(FoundedProducts.get(j)));
                    Actions moveAndClickOnNameOfProduct = new Actions(Driver);
                    Js.executeScript("arguments[0].scrollIntoView();", FoundedProducts.get(j));
                    moveAndClickOnNameOfProduct.moveToElement(FoundedProducts.get(j)).pause(Duration.ofSeconds(1)).click().build().perform();
                    break exit;
                }
            }

            //if on page was less than 12 product we check all products
            Assert.assertTrue(FoundedProducts.size() == 12, "Product didnt find by name.");

            //go to next page
            Wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//nav[@class='pagination']/div[@class='col-md-4']")));
            Wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@rel='next']")));
            String PressToBtngoNextPage  = "document.getElementsByClassName('next js-search-link')[0].click()";
            Js.executeScript(PressToBtngoNextPage);

            //wait for load new products on page
            try{
                Wait.until(ExpectedConditions.and(
                        ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='col-md-6 hidden-sm-down total-products']/p")),
                        ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(Driver.findElement(By.xpath("//div[@class='col-md-6 hidden-sm-down total-products']/p")), "12"))
                ));
            }catch (StaleElementReferenceException ex){
                Wait.until(ExpectedConditions.and(
                        ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='col-md-6 hidden-sm-down total-products']/p")),
                        ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(Driver.findElement(By.xpath("//div[@class='col-md-6 hidden-sm-down total-products']/p")), "12"))
                ));
            }

        }

        //check name of product
        Wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@id='product-availability']")));
        CustomReporter.logAction("Products page was open");
        Wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[@itemprop='name']")));
        WebElement FoundName = Driver.findElement(By.xpath("//h1[@itemprop='name']"));
        Wait.until(ExpectedConditions.visibilityOf(FoundName));
        String stFoundName = FoundName.getText();
        CustomReporter.logAction("Name of found product is " + stFoundName.toLowerCase());
        Assert.assertEquals(stFoundName.toLowerCase(), Product.getName().toLowerCase(), "Name didnt match.");

        //search price of added product and compare
        Wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@itemprop='price']")));
        WebElement PriceOfProduct = Driver.findElement(By.xpath("//span[@itemprop='price']"));
        String FoundPrice = PriceOfProduct.getText().replace("₴", "").replace(" ", "").replace(",", ".");
        CustomReporter.log("Found price: " + FoundPrice);
        Assert.assertEquals(FoundPrice, Product.getPrice(), "Price didnt match.");

        //check is quantity not 0
        Wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@id='product-availability']")));
        WebElement Quantity = Driver.findElement(By.xpath("//span[@id='product-availability']"));
        Assert.assertNotEquals("Нет в наличии",Quantity.getText().replace("\uE14B", "").substring(1), "Quantity is 0");

        //press to button Products detail
        Wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[(@class='nav-link') or (@class='nav-link active')]")));
        WebElement BtnDeteilProducts = Driver.findElement(By.xpath("//a[(@class='nav-link') or (@class='nav-link active')]"));
        Wait.until(ExpectedConditions.elementToBeClickable(BtnDeteilProducts));
        BtnDeteilProducts.click();

        //check quantity of product
        Wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='product-quantities']/span")));
        String QuantityOfProduct = Driver.findElement(By.xpath("//div[@class='product-quantities']/span")).getText().replaceAll("[^0-9,]", "");
        CustomReporter.log("Found quantity is " + QuantityOfProduct);
        Assert.assertEquals(Product.getQty(), QuantityOfProduct, "Quantity didnt match" );
        CustomReporter.logAction("Quantity of product is: " + QuantityOfProduct);
    }


}
