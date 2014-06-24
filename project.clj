(defproject maybe "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :repositories {"sonartype releases" "https://oss.sonatype.org/content/repositories/releases"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.functionaljava/functionaljava "4.1"]]
  :javac-options ["-target" "1.7" "-source" "1.7" "-Xlint:-options"]
  :java-source-paths ["src/java" "test/java"])
