package com.example.franco.testids;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

/*
    Code ripped directly from the python scripts used to conduct manual testing
 */
public class Preprocessing {

    public Preprocessing()
    {
    }

    /*
    Reads in the logs line by line from the textfile they were sent to on the device itself.
     */
    public List<String> readLogFile(String pathToLogFile)
    {
        List<String> logs = new ArrayList<String>();
        try {
            Scanner s = new Scanner(new File(pathToLogFile));
            while (s.hasNextLine())
            {
                logs.add(s.nextLine());
            }
            s.close();
        } catch (IOException ex)
        {
            logs.add("file does not exist");
        }

        return logs;
    }

    /*
    Creates the array that is fed to BINKS.
     */
    public List<Float> frequencyAnalysis(List<String> logs)
    {
        String[] headers = {"\'mprotect","llseek","clock_gettime","clone","close","dup","epoll_ctl","epoll_pwait","faccessat","fchmodat","fcntl64","fdatasync","fstat64","fstatat64","fsync","ftruncate64","futex","geteuid32","getpid","getrandom","getsockopt","gettimeofday","getuid32","ioctl","lseek","madvise","mmap2","mprotect","munmap","openat","prctl","pread64","process_vm_readv","pwrite64","read", "readlinkat","recvfrom","renameat","rt_sigprocmask","rt_sigreturn","rt_sigsuspend", "sched_yield","sendmsg","sendto","sigreturn","unlinkat","write","writev"};
        int[] headerFrequency = new int[48];

        String lineCheck;
        String currentHeader;
        int headerCheck;

        List<Float> frequencyList = new ArrayList<Float>();
        ListIterator<String> logListIterator = logs.listIterator();


        for(int i = 0; i < logs.size(); i++)
        {
            lineCheck = logListIterator.next();
            headerCheck = lineCheck.indexOf('(');
            if(headerCheck != -1)
            {
                currentHeader = lineCheck.substring(0, headerCheck);
                for(int j = 0; j < headers.length; j++)
                {
                    if(headers[j].equals(currentHeader))
                    {
                        headerFrequency[j] += 1;
                    }
                }
            }
        }

        for(int i = 0; i < headerFrequency.length; i++)
        {
            frequencyList.add((float)headerFrequency[i]/logs.size());
        }
      
       return frequencyList;
    }

}
