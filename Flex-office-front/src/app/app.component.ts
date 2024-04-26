import { Component } from '@angular/core';
import { UserService } from './services/user.service';
import { OnInit } from '@angular/core';
import { Router } from '@angular/router';



@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  providers : [ UserService ]
})
export class AppComponent implements OnInit {
  title = 'Flex-office-front';
  utilisateurs: any[] = [];


  constructor(private userService : UserService , private router: Router){

  }
  ngOnInit(): void {
    console.log('OnInit ...');
    this.userService.getUsers().subscribe((data) => {
      this.utilisateurs = Object.values(data);    });
  }

  redirectLogin(){

    this.router.navigate(['/login']);
  }
}
