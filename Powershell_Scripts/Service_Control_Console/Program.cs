using Service_Control_Console;
using System;
using System.ServiceProcess;

class Program
{
    static void Main()
    {
        Service_Control services = new Service_Control();

        services.hello();


    }
}
