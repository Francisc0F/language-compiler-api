package francisco.languagecompiler.resource.service;

import francisco.languagecompiler.resource.model.Build;
import org.springframework.stereotype.Service;

@Service
public class BuildsService extends BaseService<Build> {

    public BuildsService() {
        Build.Builder b = new Build.Builder();
        Build java = b.Java("helloWorld", "public class HelloWorld {\n public static void main(String[] args) {\n" + "        System.out.println(\"Hello, World!\");\n" + "    }\n" + "}");
        Build node = b.Js("helloWorld", "public class HelloWorld {\n public static void main(String[] args) {\n" + "        System.out.println(\"Hello, World!\");\n" + "    }\n" + "}");
        Build cCorrect = b.C("5d4e1e78-804b-4417-a4c7-fbcf2eddd8ea", "C Build", "#include <stdio.h>\n int main() {\n printf(\"Hello, World!\");\n return 0;\n}");
        Build cError= b.C("C Build with Error", "#include <stdio.h>\n int maXin() {\n printf(\"Hello, World!\");\n return 0;\n}");

        items.add(java);
        items.add(cCorrect);
        items.add(node);
        items.add(cError);

    }
}