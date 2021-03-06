﻿using PolyPaint.Utilitaires;
using System.Windows.Input;


namespace PolyPaint.VueModeles
{
    public class GameModeMenuViewModel : BaseViewModel, IPageViewModel
    {

        public override string GetCurrentViewModelName()
        {
            return "GameModeMenuViewModel";
        }

        private ICommand _goToDrawingWindow;
        private ICommand _goToGameMenu;
        private ICommand _goToUserProfile;
        private ICommand _goToGuessingView;
        private ICommand _goToFreeForAll;
        private ICommand _goToGameChoice;

        public ICommand GoToDrawingWindow
        {
            get
            {
                return _goToDrawingWindow ?? (_goToDrawingWindow = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToDrawingWindow", "");
                }));
            }
        }

        public ICommand GoToGameMenu
        {
            get
            {
                return _goToGameMenu ?? (_goToGameMenu = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToGameMenu", "");
                }));
            }
        }

        public ICommand GoToUserProfile
        {
            get
            {
                return _goToUserProfile ?? (_goToUserProfile = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToUserProfile", "");
                }));
            }
        }

        public ICommand GoToGuessingView
        {
            get
            {
                return _goToGuessingView ?? (_goToGuessingView = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToGuessingView", "");
                }));
            }
        }

        public ICommand GoToFreeForAll
        {
            get
            {
                return _goToFreeForAll ?? (_goToFreeForAll = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToFreeForAll", "");
                }));
            }
        }

        public ICommand GoToGameChoice
        {
            get
            {
                return _goToGameChoice ?? (_goToGameChoice = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToGameChoice", "");
                }));
            }
        }
    }
}