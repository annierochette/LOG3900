using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;

namespace PolyPaint.Modeles
{
    public sealed class User 
    {
        public static User instance = null;
        private static readonly object padlock = new object();

        public static User Instance
        {
            get
            {
                lock (padlock)
                {
                    if (instance == null)
                    {
                        instance = new User();
                    }
                    return instance;
                }
            }
        }

        public string Username { get; set; }

        public string Firstname { get; set; }

        public string Lastname { get; set; }

        public string Token { get; set; }

        public string Id { get; set; }

        //public event PropertyChangedEventHandler PropertyChanged;


        //protected void ChangeProperty([CallerMemberName] string propertyName = null)
        //{
        //    PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        //}
    }

}
