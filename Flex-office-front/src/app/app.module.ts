import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { RegisterComponent } from './component/register/register.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DashboardComponent } from './component/dashboard/dashboard.component';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './component/login/login.component';
import { VisualisationComponent } from './component//visualisation/visualisation.component';
import { BureauComponent } from './component/bureau/bureau.component';
import { EquipeComponent } from './component/equipe/equipe.component';
import { TooltipModule } from 'ngx-bootstrap/tooltip';
import { IndexComponent } from './component/index/index.component';

@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    DashboardComponent,
    LoginComponent,
    VisualisationComponent,
    BureauComponent,
    EquipeComponent,
    IndexComponent,
  ],
  imports: [
    BrowserModule,
    NgbModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    CommonModule,
    ReactiveFormsModule,
    TooltipModule.forRoot(),

  ],
  providers: [
    provideClientHydration()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
