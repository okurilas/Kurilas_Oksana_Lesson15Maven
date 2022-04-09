import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources("classpath:config.properties")
public interface ConfigServer extends Config {

    @Key("url1")
    String url1();

    @Key("url2")
    String url2();

    @Key("url3")
    String url3();

    @Key("login")
    String login();

    @Key("pwd")
    String pwd();
}