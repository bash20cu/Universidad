

using OpenQA.Selenium;
using OpenQA.Selenium.Chrome;
using OpenQA.Selenium.Remote;
using OpenQA.Selenium.Support.UI;

class Program
{
    static void Main()
    {
        string loginUrl = "https://www.saucedemo.com/";

        string usuario = "standard_user";
        string clave = "secret_sauce";

        ChromeOptions options = new ChromeOptions();
        options.AddArgument("--start-maximized");

        IWebDriver driver = new RemoteWebDriver(
            new Uri("http://localhost:4444"),
            options
        );

        try
        {
            Console.WriteLine("================================");
            Console.WriteLine("INICIANDO PRUEBA SAUCEDEMO");
            Console.WriteLine("================================");

            driver.Navigate().GoToUrl(loginUrl);

            WebDriverWait wait =
                new WebDriverWait(driver, TimeSpan.FromSeconds(20));

            // LOGIN

            IWebElement txtUsuario =
                wait.Until(
                    d => d.FindElement(By.Id("user-name"))
                );

            IWebElement txtClave =
                driver.FindElement(By.Id("password"));

            IWebElement btnLogin =
                driver.FindElement(By.Id("login-button"));

            txtUsuario.Clear();
            txtUsuario.SendKeys(usuario);

            txtClave.Clear();
            txtClave.SendKeys(clave);

            ((ITakesScreenshot)driver)
                .GetScreenshot()
                .SaveAsFile("01_Login.png");

            Console.WriteLine("Ejecutando Login...");

            btnLogin.Click();

            wait.Until(
                d => d.Url.Contains("inventory")
            );

            Console.WriteLine("✅ Login Exitoso");

            ((ITakesScreenshot)driver)
                .GetScreenshot()
                .SaveAsFile("02_Inventory.png");

            // AGREGAR PRODUCTO

            IWebElement btnAgregar =
                wait.Until(
                    d => d.FindElement(
                        By.Id("add-to-cart-sauce-labs-backpack"))
                );

            btnAgregar.Click();

            Console.WriteLine(
                "✅ Producto agregado al carrito");

            IWebElement badge =
                driver.FindElement(
                    By.ClassName("shopping_cart_badge"));

            Console.WriteLine(
                $"Productos en carrito: {badge.Text}");

            ((ITakesScreenshot)driver)
                .GetScreenshot()
                .SaveAsFile("03_Carrito.png");

            // ABRIR CARRITO

            IWebElement carrito =
                driver.FindElement(
                    By.ClassName("shopping_cart_link"));

            carrito.Click();

            wait.Until(
                d => d.Url.Contains("cart")
            );

            Console.WriteLine(
                "✅ Carrito abierto correctamente");

            // VALIDAR PRODUCTO

            IWebElement producto =
                driver.FindElement(
                    By.ClassName("inventory_item_name"));

            string nombreProducto =
                producto.Text.Trim();

            Console.WriteLine(
                $"Producto encontrado: {nombreProducto}");

            if (nombreProducto ==
                "Sauce Labs Backpack")
            {
                Console.WriteLine(
                    "✅ Validación correcta");
            }
            else
            {
                Console.WriteLine(
                    "❌ Producto incorrecto");
            }

            ((ITakesScreenshot)driver)
                .GetScreenshot()
                .SaveAsFile("04_ProductoValidado.png");

            // GUARDAR HTML

            File.WriteAllText(
                "PaginaResultado.html",
                driver.PageSource
            );

            // LOGOUT

            IWebElement menu =
                driver.FindElement(
                    By.Id("react-burger-menu-btn"));

            menu.Click();

            Thread.Sleep(1000);

            IWebElement logout =
                wait.Until(
                    d => d.FindElement(
                        By.Id("logout_sidebar_link"))
                );

            logout.Click();

            wait.Until(
                d => d.Url.Contains("saucedemo")
            );

            Console.WriteLine(
                "✅ Logout ejecutado");

            ((ITakesScreenshot)driver)
                .GetScreenshot()
                .SaveAsFile("05_Logout.png");

            Console.WriteLine();
            Console.WriteLine("================================");
            Console.WriteLine("PRUEBA FINALIZADA");
            Console.WriteLine("================================");

            Console.WriteLine($"URL: {driver.Url}");
            Console.WriteLine($"Título: {driver.Title}");
        }
        catch (Exception ex)
        {
            Console.WriteLine();
            Console.WriteLine("================================");
            Console.WriteLine("ERROR");
            Console.WriteLine("================================");

            Console.WriteLine(ex.ToString());

            try
            {
                ((ITakesScreenshot)driver)
                    .GetScreenshot()
                    .SaveAsFile("ERROR.png");
            }
            catch
            {
            }
        }
        finally
        {
            Console.WriteLine();
            Console.WriteLine("Cerrando navegador...");

            driver.Quit();
        }
    }
}
