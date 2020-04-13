using PolyPaint.VueModeles;
using PolyPaint.Vues;
using PolyPaint.Utilitaires;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Linq;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Media;

namespace PolyPaint
{
    /// <summary>
    /// Logique d'interaction pour App.xaml
    /// </summary>
    public partial class App : Application
    {
        protected override void OnStartup(StartupEventArgs e)
        {
            base.OnStartup(e);

            LoginControlWindow app = new LoginControlWindow();
            LoginControlViewModel context = new LoginControlViewModel();
            app.DataContext = context;
            app.Show();
        }

        protected void Disconnect(object sender, ExitEventArgs e)  
        {
            var socket = AppSocket.Instance;
            socket.Close();
        }
    }
}
