import { Component } from '@angular/core';
import { UserService } from './user.service';
import { OnInit } from '@angular/core';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  providers : [ UserService ]
})
export class AppComponent implements OnInit {
  title = 'Flex-office-front';
  users: any[] = [];


  constructor(private userService : UserService){

  }
  ngOnInit(): void {
    console.log('OnInit ...');
    this.userService.getUsers().subscribe((data) => {
      this.users = Object.values(data);    });
  }
}
