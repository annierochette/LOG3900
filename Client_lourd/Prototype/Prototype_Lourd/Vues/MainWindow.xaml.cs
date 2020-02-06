using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Net.Sockets;

namespace Prototype_Lourd
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {

        string serverIP = "localhost";
        int port = 8080;

        public MainWindow()
        {
            InitializeComponent();
        }

        private void connectToIPAddress(object sender, RoutedEventArgs e)
        {
            TcpClient client = new TcpClient(serverIP, port);
        }

        private void Main_Navigated(object sender, NavigationEventArgs e)
        {

        }

        private void button1_Click(object sender, RoutedEventArgs e)
        {

        }

        private void textBox1_TextChanged(object sender, TextChangedEventArgs e)
        {

        }

        private void choosePseudonym_Click(object sender, RoutedEventArgs e)
        {

        }

        private void sendMessage_Click(object sender, RoutedEventArgs e)
        {

            if (!String.IsNullOrWhiteSpace(this.messageBox.Text))
            {
                String timeStamp = GetTimestamp(DateTime.Now);
                messageList.Items.Add(timeStamp + " " + this.messageBox.Text);
            }
            this.messageBox.Text = "";
        }

        public static String GetTimestamp(DateTime value)
        {
            return value.ToString("HH:mm:ss");
        }
    }
}
