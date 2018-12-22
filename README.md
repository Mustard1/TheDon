# TheDon
Smart Android IDS

Hi DHA

## Tools Used to Create TheDon and Conduct Testing
1. Genymotion
2. Tensorflow
3. Android Studio

### How to extract kernel calls

Below are the tools and commands used to retrieve the kernel calls from an application, the assumption being the device is virtual although they should still function if the device is rooted:

Command to launch and ADB shell:   
```` adb shell su ````

Tool and command used to collect kernel calls:   
```` strace -p [PID] ````

Variant of above - Tool and command used to send logs to file:
```` strace -p [PID] &> \path\to\filename ````

Tool and command used to fuzz input to the application:
```` monkey -p [name of application from process list] -v --ignore crashes 500 ````

Command used to transfer the logs out of our rooted device:  
```` adb pull [\path\to\filename]  [path\to\destination] ````

The 25 malware samples in testing were taken from a pool of the drebin and GNOME repositories. Opposite of this the benign applications were ripped directly from the play store and chosen based on network activity and overall popularity. Final percentage of successful ID's was 94%.
