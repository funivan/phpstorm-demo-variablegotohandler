package org.funivan.phpstorm.demo.VariableGoToHandler;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import org.jetbrains.annotations.Nullable;

/**
 * Created by funivan on 11/12/14.
 */
public class GotToVariableDeclaration implements GotoDeclarationHandler {

    @Nullable
    @Override
    public PsiElement[] getGotoDeclarationTargets(@Nullable PsiElement psiElement, int i, Editor editor) {

        Boolean isVariable = PlatformPatterns.psiElement(PhpTokenTypes.VARIABLE).accepts(psiElement);

        if (!isVariable) {
            return null;
        }

        String filePath = getFilePath(psiElement);
        if (filePath == null) {
            return null;
        }

        String regex = "^(.*).phtml$";

        if (filePath.matches(regex) == false) {
            return null;
        }

        filePath = filePath.replaceAll(regex, "$1.php");
        String variableName = psiElement.getText().substring(1);

        VirtualFile virtualFile = VfsUtil.findRelativeFile(filePath, psiElement.getProject().getBaseDir());
        if (virtualFile == null) {
            return null;
        }
        PsiFile psiFile = PsiManager.getInstance(psiElement.getProject()).findFile(virtualFile);

        if (psiFile == null) {
            return null;
        }

        // traverse all methods in file $this->set()
        PsiElement[] elements = PsiTreeUtil.collectElements(psiFile, new ElementFinder(variableName));

        return elements;
    }

    public static String getFilePath(PsiElement e) {
        PsiFile containingFile = e.getOriginalElement().getContainingFile();

        if (containingFile == null) {
            return null;
        }

        PsiFile originalFile = containingFile.getOriginalFile();

        if (originalFile == null) {
            return null;
        }

        VirtualFile virtualFile = originalFile.getVirtualFile();
        if (virtualFile == null) {
            return null;
        }

        String path = virtualFile.getPath();
        if (path == null) {
            return null;
        }

        return path;
    }

    @Nullable
    @Override

    public String getActionText(DataContext dataContext) {
        return null;
    }
}
