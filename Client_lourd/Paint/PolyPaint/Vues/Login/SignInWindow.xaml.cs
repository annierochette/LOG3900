using System;
using System.Net.Http;
using Newtonsoft.Json;
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
using PolyPaint.VueModeles;
using PolyPaint.Utilitaires;

namespace PolyPaint.Vues
{

    public partial class SignInWindow : UserControl
    {
        public SignInWindow()
        {
            InitializeComponent();
        }

        public class player
        {
            [JsonProperty("firstName")]
            public string firstName { get; set; }

            [JsonProperty("lastName")]
            public string lastName { get; set; }

            [JsonProperty("username")]
            public string username { get; set; }

            [JsonProperty("password")]
            public string password { get; set; }
        }

        private async void UserSign(object sender, RoutedEventArgs e)
        {
            string firstName = surnameBox.Text;
            string lastName = nameBox.Text;
            string username = usernameBox.Text;
            string password = passBox.Password;
            string repass = rePassBox.Password;

            if (repass == password)
            {

                var HttpClient = new HttpClient();
                var infos = new player { 
                    firstName = firstName,
                    lastName = lastName, 
                    username = username,
                    password = password
                };


                var json = await Task.Run(() => JsonConvert.SerializeObject(infos));
                var httpContent = new StringContent(json, Encoding.UTF8, "application/json");

                var res = await HttpClient.PostAsync(Constants.ADDR + "players", httpContent);
                if (res.Content != null)
                {
                    var responseContent = await res.Content.ReadAsStringAsync();
                    Console.WriteLine(responseContent);
                    System.Windows.MessageBox.Show("Votre compte à bien été créé!", "Succès");
                    ((SignInViewModel)(DataContext)).GoToLogin();
                }
                else { 
                    var responseContent = await res.Content.ReadAsStringAsync();
                    Console.WriteLine("marche pas: " + responseContent); }
                Console.WriteLine(password);
            }
            else { System.Windows.MessageBox.Show("le mot de passe et la vérification ne correspondent pas", "Erreur"); }
        }

        private void surnameBox_TextChanged(object sender, TextChangedEventArgs e)
        {
            if (surnameBox.Text.Length > 0)
                tbsurname.Visibility = Visibility.Collapsed;
            else
                tbsurname.Visibility = Visibility.Visible;
        }
        private void nameBox_TextChanged(object sender, TextChangedEventArgs e)
        {
            if (nameBox.Text.Length > 0)
                tbname.Visibility = Visibility.Collapsed;
            else
                tbname.Visibility = Visibility.Visible;
        }

        private void usernameBox_TextChanged(object sender, TextChangedEventArgs e)
        {
            if (usernameBox.Text.Length > 0)
                tbusername.Visibility = Visibility.Collapsed;
            else
                tbusername.Visibility = Visibility.Visible;
        }

        private void passBox_PasswordChanged(object sender, RoutedEventArgs e)
        {
            if (passBox.Password.Length > 0)
                tbpassword.Visibility = Visibility.Collapsed;
            else
                tbpassword.Visibility = Visibility.Visible;
            if (rePassBox.Password.Length >= 7 && passBox.Password.Length >= 7)
            {
                connection.IsEnabled = true;
            }
            else connection.IsEnabled = false;
        }

        private void rePassBox_PasswordChanged(object sender, RoutedEventArgs e)
        {
            if (rePassBox.Password.Length > 0)
                tbrepassword.Visibility = Visibility.Collapsed;
            else
                tbrepassword.Visibility = Visibility.Visible;
            if (rePassBox.Password.Length >= 7 && passBox.Password.Length >= 7)
            {
                connection.IsEnabled = true;
            } else connection.IsEnabled= false;
        }
    }
}
