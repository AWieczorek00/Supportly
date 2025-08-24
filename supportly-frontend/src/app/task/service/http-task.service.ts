import { Injectable } from '@angular/core';
import {Task} from '../Task';
import {BaseApiService} from '../../config/base-api.service';
import {TaskCriteria} from '../TaskCriteria';

@Injectable({
  providedIn: 'root'
})
export class HttpTaskService extends BaseApiService<TaskCriteria, Task>{
  protected resourcePath = 'task';



}
