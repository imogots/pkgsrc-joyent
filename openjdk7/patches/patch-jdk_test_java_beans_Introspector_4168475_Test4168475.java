$NetBSD$

--- jdk/test/java/beans/Introspector/4168475/Test4168475.java.orig	2013-02-20 17:07:30.000000000 +0000
+++ jdk/test/java/beans/Introspector/4168475/Test4168475.java
@@ -41,6 +41,8 @@ public class Test4168475 {
     private static final String[] PATH = {"infos"};
 
     public static void main(String[] args) throws IntrospectionException {
+        // running under jtreg -agentvm: system classloader can't find this class (separate classloader?)
+        Thread.currentThread().setContextClassLoader(Test4168475.class.getClassLoader());
         Introspector.setBeanInfoSearchPath(PATH);
         BeanInfo info = Introspector.getBeanInfo(Component.class);
         PropertyDescriptor[] pds = info.getPropertyDescriptors();
