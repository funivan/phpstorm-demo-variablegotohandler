package org.funivan.phpstorm.demo.VariableGoToHandler;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiElementFilter;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.ParameterList;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;

/**
 * Created by funivan on 11/12/14.
 */
public class ElementFinder implements PsiElementFilter {

    protected String variableName = "";

    public ElementFinder(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public boolean isAccepted(PsiElement psiElement) {


        Boolean result = PlatformPatterns.psiElement(StringLiteralExpression.class)
                .withParent(
                        PlatformPatterns.psiElement(ParameterList.class)
                                .withParent(
                                        PlatformPatterns.psiElement(MethodReference.class)
                                )
                ).accepts(psiElement);

        if (!result) {
            return result;
        }


        if (psiElement.getText().matches("^[\"\']" + variableName + "[\"']$") == false) {
//            System.out.println("Invalid name:" + psiElement.getText());
            return false;
        }

        PsiElement[] parametersList = ((ParameterList) psiElement.getParent()).getParameters();

        if (parametersList[0].equals(psiElement) == false) {
//            System.out.println("Not first argument:" + psiElement.getText());
            return false;
        }


        MethodReference methodRef = (MethodReference) psiElement.getParent().getParent();
        if (methodRef.getName().matches("set") == false) {
//        System.out.println("Invalid method name::" + methodRef.getName());
            return false;
        }


        PsiElement el = methodRef.getFirstChild();

        if (el == null || el.getText().matches("^\\$this$") == false) {
            return false;
        }


        return true;
    }
}
