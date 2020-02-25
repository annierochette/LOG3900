using System;
using System.Net.Http;
using Newtonsoft.Json;
using PolyPaint.Utilitaires;
using System.Collections.Generic;
using System.Web;
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
using System.Windows.Shapes;
using System.ComponentModel;
using System.Runtime.CompilerServices;



namespace PolyPaint.Vues
{

    public partial class LoginWindow : UserControl
    {
        public LoginWindow()
        { 
            InitializeComponent();
        }

        public class loginPlayer
        {

            [JsonProperty("username")]
            public string username { get; set; }

            [JsonProperty("password")]
            public string password { get; set; }
        }

        public bool IsEnable
        {
            get { return isEnable; }
            set
            {
                isEnable = value;
                NotifyPropertyChanged();
            }
        }

        private async void UserConnect(object sender, RoutedEventArgs e)
        {
            isEnable = false;
            string username = usernameBox.Text;
            string password = passwordBox.Password;

            var HttpClient = new HttpClient();
            var infos = new loginPlayer
            {
                username = username,
                password = password
            };

            var json = await Task.Run(() => JsonConvert.SerializeObject(infos));
            var httpContent = new StringContent(json, Encoding.UTF8, "application/json");

            var res = await HttpClient.PostAsync("http://localhost:5050/players/login", httpContent);
            if (res.Content != null)
            {
                var responseContent = await res.Content.ReadAsStringAsync();
                Console.WriteLine(responseContent);

            }
            if (res.StatusCode.ToString() == "201")
            {
                isEnable = true;
            }

            Console.WriteLine(username);
            Console.WriteLine(password);
        }

        private void usernameBox_TextChanged(object sender, TextChangedEventArgs e)
        {
            if (usernameBox.Text.Length > 0)
                tbuser.Visibility = Visibility.Collapsed;
            else
                tbuser.Visibility = Visibility.Visible;
        }

        private void passwordBox_PasswordChanged(object sender, RoutedEventArgs e)
        {
            if (passwordBox.Password.Length > 0)
                tbpass.Visibility = Visibility.Collapsed;
            else
                tbpass.Visibility = Visibility.Visible;
        }
    }
}