package modules;

import com.google.inject.AbstractModule;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;

public class ShiroModule  extends AbstractModule {
    @Override
    protected void configure() {

        Factory<SecurityManager> factory = new IniSecurityManagerFactory("conf/shiro.ini");
        SecurityManager securityManager = factory.getInstance();

        SecurityUtils.setSecurityManager(securityManager);
    }
}
