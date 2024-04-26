import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from './component/register/register.component';
import { DashboardComponent } from './component/dashboard/dashboard.component';
import { LoginComponent } from './component/login/login.component';
import { IndexComponent } from './component/index/index.component';
import { VisualisationComponent } from './component/visualisation/visualisation.component';
import { EquipeComponent } from './component/equipe/equipe.component';
import { BureauComponent } from './component/bureau/bureau.component';
import { AppComponent } from './app.component';

const routes: Routes = [

  { path: ' ',redirectTo: '/index', pathMatch: 'full'},
  { path: "index", component: IndexComponent },
  { path: "register", component: RegisterComponent },
  { path: "login", component: LoginComponent },
  { path: "dashboard", component: DashboardComponent },
  { path: "visualisation", component: VisualisationComponent },
  { path: "equipe", component: EquipeComponent},
  { path: "bureau", component: BureauComponent },
  {path: '**', redirectTo: '/index'},


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
