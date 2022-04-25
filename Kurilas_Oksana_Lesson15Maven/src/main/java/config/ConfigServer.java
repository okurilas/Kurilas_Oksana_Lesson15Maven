package config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;


@Sources("classpath:config.properties")
public interface ConfigServer extends Config {


    @Key("urlOTUS")
    String urlOTUS();

    @Key("urlDUCK")
    String urlDUCK();

    @Key("urlDEMO")
    String urlDEMO();

    @Key("login")
    String login();

    @Key("pwd")
    String pwd();
}