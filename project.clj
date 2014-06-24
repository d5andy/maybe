(defproject maybe "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :repositories {"sonartype releases" "https://oss.sonatype.org/content/repositories/releases"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.functionaljava/functionaljava "4.1"]
                 [junit/junit "4.11"]]
  :plugins [[lein-junit "1.1.2"]]
  :javac-options ["-target" "1.7" "-source" "1.7" "-Xlint:-options"]
  :java-source-paths ["src/java" "test/java"]
  :junit ["test/java"]
  :jvm-opts ["-XX:MaxPermSize=128m"])
