# screenplay-web
An example of screenplay pattern used for web automation

* example of POM
* web driver management delegated to junit 5 extension (supports parallel execution)
* tracing and screenshots attachment to report implemented using aspects
* steps (interaction/questions) implemented using lambda expressions
* wrappers for web elements (should allow to interact with screen elements using different techniques, for example native selenium call or js execution without introducing helper methods - use polymorphism instead)
* POM divided to view classes (responsible for finding web elements) and model classes (responsible for element actions implementation) -> convenient methods implementation moved to dedicated classes (steps factories)
* steps can use methods chaining to switch between different pages or can be build from already existing steps, for example screenplay.interactions.CreateAccount and screenplay.interactions.FindProductDetails
* cross-browser testing supported via junit test-templates -> see SeleniumJupiter extensions documentation https://bonigarcia.dev/selenium-jupiter/
* jsoup can be used for html parsing if needed to extract data from tables, see ProductDetailsPage for trivial example
* allure can be used to generate test report with screenshots for failing tests and more readable test steps names (for drill down) -> see documentation for more details https://docs.qameta.io/allure/

Tracing example (includes values passed to methods and exception logging)

2021-08-18 09:02:32,743 INFO [ForkJoinPool-1-worker-23] io.github.glytching.junit.extension.watcher.WatcherExtension.beforeTestExecution:86 - Starting test [smarterSearchForProduct]

2021-08-18 09:02:44,172 DEBUG [ForkJoinPool-1-worker-27] pom.framework.aspects.AbstractLogAspect.logMethodExecution:31 - running: AuthenticationPage.enterIntoEmailInput(jkhzEiEMUrC@terefere.terefere)

2021-08-18 09:02:44,174 DEBUG [ForkJoinPool-1-worker-27] pom.framework.aspects.AbstractLogAspect.logMethodExecution:31 - running: AuthenticationPageView.getEmailInput()

2021-08-18 09:02:44,366 DEBUG [ForkJoinPool-1-worker-23] pom.framework.aspects.AbstractLogAspect.logMethodExecution:31 - running: ProductDetails.getDetails()

2021-08-18 09:02:44,388 DEBUG [ForkJoinPool-1-worker-23] pom.framework.aspects.AbstractLogAspect.logMethodExecution:31 - running: BrowseTheWeb.as(Actor[abilities={class screenplay.abilities.BrowseTheWeb=screenplay.abilities.BrowseTheWeb@437f5ca4},memory={}])

2021-08-18 09:02:44,388 DEBUG [ForkJoinPool-1-worker-23] pom.framework.aspects.AbstractLogAspect.logMethodExecution:31 - running: BrowseTheWeb.getDriver()

2021-08-18 09:02:44,389 DEBUG [ForkJoinPool-1-worker-23] pom.framework.aspects.AbstractLogAspect.logMethodExecution:31 - running: ProductDetailsPage.getDetails()

2021-08-18 09:02:44,392 DEBUG [ForkJoinPool-1-worker-23] pom.framework.aspects.AbstractLogAspect.logMethodExecution:31 - running: ProductDetailsPageView.getPriceElement()

2021-08-18 09:02:44,530 DEBUG [ForkJoinPool-1-worker-23] pom.framework.aspects.AbstractLogAspect.logMethodExecution:31 - running: ProductDetailsPageView.getShortDescriptionElement()

2021-08-18 09:02:44,869 DEBUG [ForkJoinPool-1-worker-27] pom.framework.aspects.AbstractLogAspect.logMethodExecution:31 - running: AuthenticationPage.clickCreateAccountButton()

2021-08-18 09:02:44,871 DEBUG [ForkJoinPool-1-worker-27] pom.framework.aspects.AbstractLogAspect.logMethodExecution:31 - running: AuthenticationPageView.getCreateAccountButton()

2021-08-18 09:02:45,048 ERROR [ForkJoinPool-1-worker-23] pom.framework.aspects.TestExceptionLoggerAspect.logException:25 - SimpleTest.smarterSearchForProduct caused java.lang.AssertionError:

Expecting:
<ProductDetails{price='$16.40', shortDescription='Printed chiffon knee length dress with tank straps. Deep v-neckline.'}>
to be <hasProperty("price", "$17.40")>

2021-08-18 09:02:47,683 INFO [ForkJoinPool-1-worker-23] io.github.glytching.junit.extension.watcher.WatcherExtension.afterTestExecution:104 - Completed test [smarterSearchForProduct] in 14936ms
