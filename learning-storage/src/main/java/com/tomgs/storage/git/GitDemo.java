package com.tomgs.storage.git;

import java.io.File;
import java.io.IOException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/**
 * @author tomgs
 * @since 2021/3/11
 */
public class GitDemo {

  public static void main(String[] args) throws GitAPIException, IOException {
    // clone
    Git.cloneRepository()
        .setURI("http://xxx.git")
        .setCredentialsProvider(new UsernamePasswordCredentialsProvider("", ""))
        .setBranch("master")
        .setDirectory(new File(""))
        .call();
    // init
    Git.init()
        .setDirectory(new File(""))
        .setGitDir(new File(""))
        .call();
    // pull
    Git git = Git.open(new File(""));
    git.pull().call();
    // add
    git.add().call();
    // commit
    git.commit().call();
    // push
    git.push().call();
    //
  }

}
