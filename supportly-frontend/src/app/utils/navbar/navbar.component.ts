import { Component } from '@angular/core';
import {MatToolbar} from '@angular/material/toolbar';
import {MatIcon} from '@angular/material/icon';
import {MatAnchor, MatIconButton} from '@angular/material/button';
import {Router} from 'express';
import {MatDialog} from '@angular/material/dialog';
import {MatMenu, MatMenuItem, MatMenuTrigger} from '@angular/material/menu';
import {RouterLink} from '@angular/router';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-navbar',
  imports: [
    MatToolbar,
    MatIcon,
    MatIconButton,
    MatMenuTrigger,
    MatMenu,
    MatAnchor,
    RouterLink,
    NgOptimizedImage,
    MatMenuItem
  ],
  templateUrl: './navbar.component.html',
  standalone: true,
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
  // constructor(private _router: Router, public dialog: MatDialog) {
  // }

  // logout() {
  //   // localStorage.clear();
  //   // this._router.navigate(['/login']);
  // }

  open() {
    // this.dialog.open(SmallWindowsMenuComponent, {
    //   height: '100%',
    //   position: {right:'0px'}
    // });
  }

  logout() {
    console.log("User logged out");
  }

  toggleMenu() {
    console.log("Menu toggled");
    // Możesz użyć toggle lub zdefiniować więcej funkcji do obsługi
  }

}
