using Newtonsoft.Json;
using System;
using System.IO;
using System.Net.Http;
using System.Runtime.Serialization.Formatters.Binary;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Ink;
using System.Windows.Input;

namespace PolyPaint.Vues
{

    public partial class NewGameForm : UserControl
    {
        
        public NewGameForm()
        {
            InitializeComponent();
            IsWordWritten = false;
        }

        public bool IsWordWritten { get; set; }
        public bool HasAtleastOneClue { get; set; }

        private void textChangedEventHandler(object sender, TextChangedEventArgs args)
        {
            if (!string.IsNullOrWhiteSpace(Word.Text))
            {
                IsWordWritten = true;
                if (HasAtleastOneClue)
                {
                    save.IsEnabled = true;
                }
            }
            else
            {
                IsWordWritten = false;
                save.IsEnabled = false;
            }
        }

        public class Game
        {

            [JsonProperty("name")]
            public string name { get; set; }

            [JsonProperty("clues")]
            public string[] clues { get; set; }

            [JsonProperty("data")]
            public byte[] data{ get; set; }
        }
        [Serializable]
        public sealed class MyCustomStrokes
        {
            public MyCustomStrokes() { }
            public Point[][] StrokeCollection;
        }

        private void MessageBox_Loaded(object sender, RoutedEventArgs e)
        {
        }

        private void Add_clue(object sender, RoutedEventArgs e)
        {
            if (!string.IsNullOrWhiteSpace(Clue.Text))
            {
                ListOfClues.Items.Add(Clue.Text);
                HasAtleastOneClue = true;
                DeleteClue.IsEnabled = true;
                if (IsWordWritten)
                    save.IsEnabled = true;
            }
            Clue.Text = "";
        }

        private void Delete_clue(object sender, EventArgs e)
        {

            if (ListOfClues.SelectedItems.Count != 0)
            {
                while (ListOfClues.SelectedIndex != -1)
                {
                    ListOfClues.Items.RemoveAt(ListOfClues.SelectedIndex);
                }
            }
            if (!ListOfClues.HasItems)
            {
                DeleteClue.IsEnabled = false;
                save.IsEnabled = false;
            }

        }

        private void save_Click(object sender, RoutedEventArgs e)
        {

        }

    } 
    
}
