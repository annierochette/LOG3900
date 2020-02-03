//using System.ComponentModel;
//using System.Linq;
//using System.Runtime.CompilerServices;
//using System.Windows.Ink;

//namespace Prototype_Lourd.Model
//{
   
//    class UserConnection : INotifyPropertyChanged
//    {
        
        

//        private string username = "";
//        private string ipAddress = "127.0.0.1";
//        public bool isConnected;

//        public string Username
//        {
//            get { return username; }
//            set { username = value; OnPropertyChanged(); }
//        }

//        public string IPAddress
//        {
//            get { return ipAddress; }
//            set { ipAddress = value; OnPropertyChanged(); }
//        }

//        public bool TextboxEnabled
//        {
//            get { return isConnected; }
//            set
//            {
//                isConnected = value;
//                OnPropertyChanged("TextboxEnabled");
//            }
//        }


       

//        public void ChooseUsername(string username) => Username = username;
//        public void SelectIpAddress(string ipAddress) => IPAddress = ipAddress;
        


//    }
//}