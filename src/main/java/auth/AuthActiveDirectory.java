package auth;

import org.apache.commons.lang.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;
import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

/**
 * @author Togrul F. Rahimli
 */
public class AuthActiveDirectory {

    private static final String CONTEXT_FACTORY_CLASS = "com.sun.jndi.ldap.LdapCtxFactory";

    public static LdapContext getConnection(String username, String password) throws NamingException, UnknownHostException {
        return getConnection(username, password, null, null);
    }

    public static LdapContext getConnection(String username, String password, String domainName) throws NamingException, UnknownHostException {
        return getConnection(username, password, domainName, null);
    }

    public static LdapContext getConnection(String username, String password, String domainName, String serverName) throws NamingException, UnknownHostException {
        try {
            if (StringUtils.isBlank(password)) return null;

            if (StringUtils.isBlank(domainName)) {
                String hostName = InetAddress.getLocalHost().getCanonicalHostName();

                if (hostName.split("\\.").length > 1) {
                    domainName = hostName.substring(hostName.indexOf(".") + 1);
                }
            }

            String principalName = username + "@" + domainName;
            String ldapURL = "ldap://" + (serverName == null ? domainName : serverName + "." + domainName) + '/';

            Hashtable<String, String> props = new Hashtable<>();
            props.put(Context.SECURITY_PRINCIPAL, principalName);
            props.put(Context.SECURITY_CREDENTIALS, password);
            props.put(Context.INITIAL_CONTEXT_FACTORY, CONTEXT_FACTORY_CLASS);
            props.put(Context.PROVIDER_URL, ldapURL);

            return new InitialLdapContext(props, null);
        } catch (CommunicationException e) {
            throw new NamingException("Failed to connect to " + domainName + (serverName == null ? "" : " through " + serverName));
        } catch (NamingException e) {
            throw new NamingException("Wrong password - " + username);
        }
    }
}
